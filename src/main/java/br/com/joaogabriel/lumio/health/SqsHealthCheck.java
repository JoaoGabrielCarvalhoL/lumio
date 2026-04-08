package br.com.joaogabriel.lumio.health;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueAttributesResponse;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;

@Readiness
@ApplicationScoped
public class SqsHealthCheck implements HealthCheck {
	
	private final SqsClient sqsClient;
	private final List<String> queueUrls;
	
	public SqsHealthCheck(SqsClient sqsClient, 
			@ConfigProperty(name = "aws.sqs.health.queues") List<String> queueUrls) {
		this.sqsClient = sqsClient;
		this.queueUrls = queueUrls;
	}

	@Override
	public HealthCheckResponse call() {
		 HealthCheckResponseBuilder builder = HealthCheckResponse.named("sqs");

		    boolean allUp = true;

		    for (String queue : queueUrls) {
		        try {
		            GetQueueAttributesResponse queueAttributes = this.sqsClient.getQueueAttributes(b -> b
		                    .queueUrl(queue)
		                    .attributeNames(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES));

		            String message = queueAttributes.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES); 

		            String queueName = extractQueueName(queue);

		            builder.withData(queueName + ".status", "UP");
		            builder.withData(queueName + ".messages", message);

		        } catch (Exception e) {
		            allUp = false;

		            String queueName = extractQueueName(queue);

		            builder.withData(queueName + ".status", "DOWN");
		            builder.withData(queueName + ".error", e.getClass().getSimpleName());
		        }
		    }

		    return builder.status(allUp).build();
	}
	
	private String extractQueueName(String queueUrl) {
	    return queueUrl.substring(queueUrl.lastIndexOf("/") + 1);
	}

}



