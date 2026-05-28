package br.com.joaogabriel.lumio.messaging.consumer;

import br.com.joaogabriel.lumio.messaging.MediaUploadProcessor;
import br.com.joaogabriel.lumio.model.dto.messaging.S3UploadEventMessage;
import br.com.joaogabriel.lumio.model.entity.ProcessedEvent;
import br.com.joaogabriel.lumio.repository.ProcessedEventRepository;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ApplicationScoped
public class S3MediaUploadConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(S3MediaUploadConsumer.class);

    private final ProcessedEventRepository processedEventRepository;
    private final Instance<MediaUploadProcessor> processors;

    public S3MediaUploadConsumer(ProcessedEventRepository processedEventRepository,
                                 Instance<MediaUploadProcessor> processors) {
        this.processedEventRepository = processedEventRepository;
        this.processors = processors;
    }

    @Incoming("video-upload-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    @RunOnVirtualThread
    @Transactional
    public void onMediaUpload(S3UploadEventMessage s3UploadEventMessage) {
        String eventId = String.format("s3-upload-%s", s3UploadEventMessage.key());
        Optional<ProcessedEvent> processedEvent = this.processedEventRepository.findByEventId(eventId);
        if (processedEvent.isEmpty()) {
            LOG.info("Received upload event for bucket: {}", s3UploadEventMessage.bucket());
            try {
                MediaUploadProcessor processor = this.processors.stream()
                        .filter(p -> p.isApplicable(s3UploadEventMessage.bucket()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No processor found for bucket: " + s3UploadEventMessage.bucket()));

                processor.process(s3UploadEventMessage);
                this.processedEventRepository.persist(new ProcessedEvent(eventId));
                LOG.info("S3 Media event successfully processed for key: {}", s3UploadEventMessage.key());
            } catch (Exception ex) {
                LOG.error("Failed to process media upload for key: {}", s3UploadEventMessage.key());
                throw ex;
            }

        } else {
            LOG.warn("Duplicate event detected. Media already processed for eventId: {}", eventId);
        }
    }
}
