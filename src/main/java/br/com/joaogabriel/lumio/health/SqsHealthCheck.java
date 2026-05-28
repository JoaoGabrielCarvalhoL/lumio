package br.com.joaogabriel.lumio.health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesRequest;

@Readiness
@ApplicationScoped
public class SqsHealthCheck implements HealthCheck {

	private final SqsClient sqsClient;
	private final String queue;

	public SqsHealthCheck(SqsClient sqsClient,
			@ConfigProperty(name = "lumio.sqs.s3.event.queue") String queueUrl) {
		this.sqsClient = sqsClient;
        this.queue = queueUrl;
	}

	@Override
	public HealthCheckResponse call() {
        try {

            sqsClient.getQueueAttributes(
                    GetQueueAttributesRequest.builder()
                            .queueUrl(queue)
                            .attributeNamesWithStrings("QueueArn")
                            .build());

            return HealthCheckResponse.named("aws-sqs")
                    .up()
                    .withData("queue", queue)
                    .build();

        } catch (Exception e) {
            return HealthCheckResponse.named("aws-sqs")
                    .down()
                    .withData("queue", queue)
                    .withData("error", e.getMessage())
                    .build();
        }
    }

}






