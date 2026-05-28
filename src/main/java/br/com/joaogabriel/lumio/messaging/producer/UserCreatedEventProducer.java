package br.com.joaogabriel.lumio.messaging.producer;

import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class UserCreatedEventProducer {
    private static final Logger LOG = LoggerFactory.getLogger(UserCreatedEventProducer.class);
    private final Emitter<String> emitter;

    public UserCreatedEventProducer(@Channel("user-notifiable-out") Emitter<String> emitter) {
        this.emitter = emitter;
    }

    public void sendSuccessNotification(final String id) {
        LOG.info("Sending success notification for user: {}", id);
        OutgoingRabbitMQMetadata metadata = new OutgoingRabbitMQMetadata.Builder()
                .withRoutingKey("user.created.success")
                .build();

        emitter.send(Message.of(id).addMetadata(metadata));
    }
}
