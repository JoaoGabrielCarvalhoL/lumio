package br.com.joaogabriel.lumio.event.producer;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.joaogabriel.lumio.client.dto.request.KeycloakCreateUserRequest;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@ApplicationScoped
public class KeycloakUserCreateQueueProducer {
	private static final Logger logger = LoggerFactory.getLogger(KeycloakUserCreateQueueProducer.class);
	
	private final SqsClient sqsClient;
	private final String queueUrl;
	
	public KeycloakUserCreateQueueProducer(SqsClient sqsClient, 
			@ConfigProperty(name = "aws.sqs.queue.keycloak-user-create") String queueUrl) {
		this.sqsClient = sqsClient;
		this.queueUrl = queueUrl;
	}
	
	public void send(KeycloakCreateUserRequest event) {
		SendMessageRequest message = SendMessageRequest.builder()
				.queueUrl(this.queueUrl)
				.messageBody("")
				.build();
		this.sqsClient.sendMessage(message);
		logger.info("Event sent to queue: {}. Payload: {}", this.queueUrl, event);
	}
}
