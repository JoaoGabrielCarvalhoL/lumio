package br.com.joaogabriel.lumio.health;

import java.net.HttpURLConnection;
import java.net.URI;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;

@Readiness
@ApplicationScoped
public class KeycloakHealthCheck implements HealthCheck {
	
	private final String oidcServerUrl;
	
	public KeycloakHealthCheck(
			@ConfigProperty(name = "quarkus.oidc.auth-server-url") String oidcServerUrl) {
		this.oidcServerUrl = oidcServerUrl;
	}

	@Override
	public HealthCheckResponse call() {
		if (this.oidcServerUrl == null || this.oidcServerUrl.isBlank()) {
			return HealthCheckResponse.named("keycloak")
                    .down()
                    .withData("reason", "OIDC URL not configured")
                    .build();
		}
		
		try {
			final URI uri = URI.create(this.oidcServerUrl + "/.well-known/openid-configuration");
			final HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.setConnectTimeout(2000);
			connection.setReadTimeout(2000);
			connection.setRequestMethod("GET");
			
			final int status = connection.getResponseCode();
			
			if (status == 200) {
				return HealthCheckResponse.named("keycloak")
	                    .up()
	                    .withData("url", this.oidcServerUrl)
	                    .build();
			} else {
				return HealthCheckResponse.named("keycloak")
		                .down()
		                .withData("status", status)
		                .build();
			}
		} catch (Exception e) {
			return HealthCheckResponse.named("keycloak")
	                .down()
	                .withData("error", e.getClass().getSimpleName())
	                .build();
		}
	}

}
