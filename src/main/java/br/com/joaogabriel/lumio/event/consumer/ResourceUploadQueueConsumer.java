package br.com.joaogabriel.lumio.event.consumer;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ResourceUploadQueueConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ResourceUploadQueueConsumer.class);

    @Incoming("resource-upload-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    @RunOnVirtualThread
    public void process(String resourceId) {
        logger.info("Processing resource: {}", resourceId);
        // Lógica para validar PDF, mover no S3, etc.
    }
}
