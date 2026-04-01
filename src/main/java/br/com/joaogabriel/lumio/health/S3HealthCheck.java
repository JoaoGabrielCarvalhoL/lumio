package br.com.joaogabriel.lumio.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.services.s3.S3Client;

@Readiness
@ApplicationScoped
public class S3HealthCheck implements HealthCheck {

	private final S3Client s3Client;
	
	public S3HealthCheck(S3Client s3Client) {
		this.s3Client = s3Client;
	}
	
	@Override
	public HealthCheckResponse call() {
		try {
	        this.s3Client.listBuckets();
	        return HealthCheckResponse.named("s3")
	        		.up()
	        		.build();

	    } catch (Exception e) {
	        return HealthCheckResponse.named("s3")
	                .down()
	                .withData("error", e.getClass().getSimpleName())
	                .build();
	    }
	}

}
