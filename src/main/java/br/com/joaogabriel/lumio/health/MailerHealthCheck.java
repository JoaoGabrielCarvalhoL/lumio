package br.com.joaogabriel.lumio.health;

import java.net.InetSocketAddress;
import java.net.Socket;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class MailerHealthCheck implements HealthCheck {
	
	private final String host;
	private final Integer port;

	public MailerHealthCheck( 
			@ConfigProperty(name = "quarkus.mailer.host") String host, 
			@ConfigProperty(name = "quarkus.mailer.port") Integer port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public HealthCheckResponse call() {
		if (this.host == null || this.host.isBlank()) {
			return HealthCheckResponse.named("mailer")
					.down()
					.withData("reason", "Host not configured.")
					.build();
		}
		
		try (final Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(this.host, this.port), 2000);
			return HealthCheckResponse.named("mailer")
                    .up()
                    .withData("host", host)
                    .withData("port", port)
                    .build();
				
		} catch (Exception e) {
			return HealthCheckResponse.named("mailer")
                    .down()
                    .withData("host", host)
                    .withData("port", port)
                    .withData("error", e.getClass().getSimpleName())
                    .build();
		} 
	}

}
