package br.com.joaogabriel.lumio.event.consumer;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class VideoUploadQueueConsumer {
    private static final Logger logger = LoggerFactory.getLogger(VideoUploadQueueConsumer.class);

    @Incoming("video-upload-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    @RunOnVirtualThread
    public void process(String videoId) {
        logger.info("Starting processing for video: {}", videoId);
        //integração com S3, transcodificação, etc.
        try {

            logger.info("Video {} processed successfully.", videoId);
        } catch (Exception e) {
            logger.error("Error processing video {}", videoId, e);
            throw new RuntimeException(e);
        }
    }
}
