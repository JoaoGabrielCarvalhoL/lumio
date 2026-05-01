package br.com.joaogabriel.lumio.event.producer;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
public class ResourceUploadQueueProducer {
    private static final Logger logger = LoggerFactory.getLogger(ResourceUploadQueueProducer.class);

    private final Emitter<String> emitter;

    public ResourceUploadQueueProducer(@Channel("resource-upload-out") Emitter<String> emitter) {
        this.emitter = emitter;
    }

    public void send(UUID resourceId) {
        emitter.send(resourceId.toString())
                .toCompletableFuture()
                .whenComplete((success, failure) -> {
                    if (failure != null) {
                        logger.error("Failed to send resource event. ID: {}", resourceId, failure);
                    } else {
                        logger.info("Resource event sent to RabbitMQ. ID: {}", resourceId);
                    }
                });
    }
}
