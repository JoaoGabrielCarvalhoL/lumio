package br.com.joaogabriel.lumio.messaging.producer;

import br.com.joaogabriel.lumio.model.dto.messaging.AwsS3DetailResponse;
import br.com.joaogabriel.lumio.model.dto.messaging.AwsS3EventResponse;
import br.com.joaogabriel.lumio.model.dto.messaging.AwsS3RecordResponse;
import br.com.joaogabriel.lumio.model.dto.messaging.S3UploadEventMessage;
import br.com.joaogabriel.lumio.model.enumerations.S3EventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Startup;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.reactive.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.List;

@ApplicationScoped
public class S3RabbitRouterProducer {

    private static final Logger LOG = LoggerFactory.getLogger(S3RabbitRouterProducer.class);

    private final Emitter<S3UploadEventMessage> emitter;
    private String queueUrl;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final ManagedExecutor  managedExecutor;

    public S3RabbitRouterProducer(@Channel("video-upload-out") Emitter<S3UploadEventMessage> emitter,
                                  @ConfigProperty(name = "lumio.sqs.s3.event.url") String queueUrl,
                                  SqsClient sqsClient, ObjectMapper objectMapper, ManagedExecutor managedExecutor) {
        this.emitter = emitter;
        this.queueUrl = queueUrl;
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.managedExecutor = managedExecutor;
    }

    public void init(@Observes Startup startup) {
        LOG.info("Scheduling SQS Polling in the background.");
        managedExecutor.runAsync(this::startQueuePolling);
    }

    private void startQueuePolling() {
        LOG.info("Initializing the SQS Polling Worker in a Virtual Thread");

        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .waitTimeSeconds(20)
                .maxNumberOfMessages(10)
                .build();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                List<software.amazon.awssdk.services.sqs.model.Message> messages = sqsClient
                        .receiveMessage(receiveMessageRequest).messages();

                if (!messages.isEmpty()) {
                    for (software.amazon.awssdk.services.sqs.model.Message message : messages) {
                        try {
                            AwsS3EventResponse awsEvent = objectMapper.readValue(message.body(), AwsS3EventResponse.class);
                            boolean hasRecords = awsEvent != null && awsEvent.records() != null && !awsEvent.records().isEmpty();
                            if (hasRecords) {
                                processAndRouteS3Event(awsEvent);
                            } else {
                                LOG.warn("Skipping message Id: {} because S3 records list is null or empty.", message.messageId());
                            }
                            sqsClient.deleteMessage(d -> d.queueUrl(queueUrl).receiptHandle(message.receiptHandle()));

                        } catch (JsonProcessingException e) {
                            LOG.error("Fatal: JSON parsing failed for message Id: {}. Stopping worker.", message.messageId(), e);
                            throw new RuntimeException("Stopping worker due to bad JSON metadata", e);
                        }
                    }
                } else {
                    LOG.debug("There are no messages in the queue currently, skipping processing.");
                }

            } catch (SqsException e) {
                LOG.error("Temporary error communicating with SQS. Retrying in 5 seconds...", e);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                LOG.error("Unexpected fatal error in SQS Worker. Shutting down worker gracefully.", e);
                Thread.currentThread().interrupt();
            }
        }
        LOG.warn("SQS Polling Worker thread has been successfully stopped.");
    }

    private void processAndRouteS3Event(AwsS3EventResponse awsEvent) {
        if (awsEvent == null || awsEvent.records().isEmpty()) {
            throw new IllegalArgumentException("AWS event must have at least one record");
        }

        for (AwsS3RecordResponse records : awsEvent.records()) {
            AwsS3DetailResponse awsS3DetailResponse = records.s3();
            String bucket = awsS3DetailResponse.awsBucket().name();
            String key = awsS3DetailResponse.awsObject().key();
            Long size = awsS3DetailResponse.awsObject().size();

            LOG.info("Routing S3 notification for key: {} from bucket: {}", key, bucket);

            S3UploadEventMessage s3UploadEventMessage =
                    new S3UploadEventMessage(bucket, key, size, S3EventType.OBJECT_CREATED);

            OutgoingRabbitMQMetadata metadata = new OutgoingRabbitMQMetadata.Builder()
                    .withRoutingKey("video-upload-key").build();
            emitter.send(Message.of(s3UploadEventMessage).addMetadata(metadata));
        }
    }
}
