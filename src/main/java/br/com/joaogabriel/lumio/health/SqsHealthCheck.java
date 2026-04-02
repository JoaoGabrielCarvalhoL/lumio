package br.com.joaogabriel.lumio.health;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.services.sqs.SqsClient;

@Readiness
@ApplicationScoped
public class SqsHealthCheck implements HealthCheck {
	
	private final SqsClient sqsClient;
	private final String queueUrl;
	
	public SqsHealthCheck(SqsClient sqsClient, 
			@ConfigProperty(name = "aws.sqs.queue.url") String queueUrl) {
		this.sqsClient = sqsClient;
		this.queueUrl = queueUrl;
	}

	@Override
	public HealthCheckResponse call() {
		try {
	        this.sqsClient.getQueueAttributes(builder -> builder
	                .queueUrl(queueUrl)
	                .attributeNamesWithStrings("All"));

	        return HealthCheckResponse.named("sqs")
	                .up()
	                .withData("queue", queueUrl)
	                .build();

	    } catch (Exception e) {
	        return HealthCheckResponse.named("sqs")
	                .down()
	                .withData("error", e.getClass().getSimpleName())
	                .build();
	    }
	}

}
