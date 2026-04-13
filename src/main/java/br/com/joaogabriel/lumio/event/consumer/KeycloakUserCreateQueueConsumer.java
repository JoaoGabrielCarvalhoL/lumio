package br.com.joaogabriel.lumio.event.consumer;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;

@ApplicationScoped
public class KeycloakUserCreateQueueConsumer {
	private static final Logger logger = LoggerFactory.getLogger(KeycloakUserCreateQueueConsumer.class);
	
	private final SqsClient sqsClient;
	private final String queueUrl; 
	private final String dlqQueueUrl;
	
	public KeycloakUserCreateQueueConsumer(SqsClient sqsClient, 
			@ConfigProperty(name = "aws.sqs.queue.keycloak-user-create") String queueUrl, 
			@ConfigProperty(name = "aws.sqs.queue.keycloak-user-create-dlq") String dlqQueueUrl) {
		this.sqsClient = sqsClient;
		this.queueUrl = queueUrl;
		this.dlqQueueUrl = dlqQueueUrl;
	}
	
	public void pollAndProcess() {
		List<Message> messages = sqsClient.receiveMessage(receiveMessage -> 
			receiveMessage.queueUrl(queueUrl).maxNumberOfMessages(10)).messages();
		
		for (Message message: messages) {
			
			/**
			 * CreateUserEvent event = JsonbBuilder.create().fromJson(m.body(), CreateUserEvent.class);

            try {
                keycloakService.createUser(event)
                    .thenAccept(kcId -> {
                        Log.info("Usuário criado no Keycloak com ID " + kcId);
                        sqsClient.deleteMessage(del -> del.queueUrl(queueUrl).receiptHandle(m.receiptHandle()));
                    })
                    .exceptionally(ex -> {
                        Log.error("Falha persistente, enviando para DLQ: " + ex.getMessage());
                        sqsClient.sendMessage(s -> s.queueUrl(dlqUrl).messageBody(m.body()));
                        sqsClient.deleteMessage(del -> del.queueUrl(queueUrl).receiptHandle(m.receiptHandle()));
                        return null;
                    });
            } catch (Exception ex) {
                Log.error("Erro no consumer: " + ex.getMessage());
            }
			 * 
			 * 
			 * */
		}
	}
	
	
	
	
	
	
	
}
