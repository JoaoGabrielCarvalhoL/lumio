package br.com.joaogabriel.lumio.event.producer;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
public class VideoUploadQueueProducer {
    private static final Logger logger = LoggerFactory.getLogger(VideoUploadQueueProducer.class);

    private final Emitter<String> emitter;

    public VideoUploadQueueProducer(@Channel("video-upload-out") Emitter<String> emitter) {
        this.emitter = emitter;
    }

    public void send(UUID videoId) {
        emitter.send(videoId.toString())
                .toCompletableFuture()
                .whenComplete((success, failure) -> {
                    if (failure != null) {
                        logger.error("Failed to send video upload event. ID: {}", videoId, failure);
                    } else {
                        logger.info("Video upload event sent to RabbitMQ. ID: {}", videoId);
                    }
                });
    }
}
