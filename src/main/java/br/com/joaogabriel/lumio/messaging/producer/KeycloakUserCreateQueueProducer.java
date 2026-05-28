package br.com.joaogabriel.lumio.messaging.producer;

import java.util.UUID;

import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class KeycloakUserCreateQueueProducer {
	private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserCreateQueueProducer.class);

	private final Emitter<String> emitter;
	
	public KeycloakUserCreateQueueProducer(@Channel("user-create-out") Emitter<String> emitter) {
		this.emitter = emitter;
	}
	
	public void send(UUID id) {
		String payload = id.toString();

		OutgoingRabbitMQMetadata metadata = OutgoingRabbitMQMetadata.builder()
				.withRoutingKey("user-create-key")
				.build();

		Message<String> message = Message.of(payload).addMetadata(metadata);

		emitter.send(message);
		LOG.info("Sent message to user-create-queue: {}", message.getPayload());
	}
}
