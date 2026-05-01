package br.com.joaogabriel.lumio.event.producer;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class KeycloakUserCreateQueueProducer {
	private static final Logger logger = LoggerFactory.getLogger(KeycloakUserCreateQueueProducer.class);

	private final Emitter<String> emitter;
	
	public KeycloakUserCreateQueueProducer(@Channel("user-create-out") Emitter<String> emitter) {
		this.emitter = emitter;
	}
	
	public void send(UUID id) {
		String payload = id.toString();
		emitter.send(payload)
				.toCompletableFuture()
				.whenComplete((success, failure) -> {
					if (failure != null) {
						logger.error("Failed to send event to RabbitMQ. Payload: {}", id, failure);
					} else {
						logger.info("Event successfully sent to RabbitMQ. Payload identifier: {}", id);
					}
				});
	}
}
