package br.com.joaogabriel.lumio.messaging.consumer;

import java.util.UUID;

import br.com.joaogabriel.lumio.exception.AlreadyProcessedException;
import br.com.joaogabriel.lumio.messaging.processor.UserProvisioningProcessor;
import io.smallrye.common.annotation.RunOnVirtualThread;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class KeycloakUserCreateQueueConsumer {
	private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserCreateQueueConsumer.class);

    private final UserProvisioningProcessor processor;

    public KeycloakUserCreateQueueConsumer(UserProvisioningProcessor processor) {
        this.processor = processor;
    }

    @Incoming("user-create-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    @RunOnVirtualThread
    public void process(String messageBody) {
        try {
            LOG.info("Receiving event from RabbitMQ: {}", messageBody);
            UUID provisioningId = UUID.fromString(messageBody);

            processor.process(provisioningId);

            LOG.info("Successfully processed provisioningId={}", provisioningId);
        } catch (AlreadyProcessedException e) {
            LOG.warn("Message already processed: {}. Skipping and acknowledging.", e.getMessage());
        } catch (Exception e) {
            LOG.error("Failed processing message body={}. Message will be retried by RabbitMQ.", messageBody, e);
            throw e;
        }
    }
}

