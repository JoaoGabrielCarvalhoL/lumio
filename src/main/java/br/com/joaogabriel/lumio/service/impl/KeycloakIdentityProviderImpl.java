package br.com.joaogabriel.lumio.service.impl;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.joaogabriel.lumio.client.KeycloakAdminTokenClient;
import br.com.joaogabriel.lumio.client.dto.response.KeycloakTokenResponse;
import br.com.joaogabriel.lumio.service.IdentityProvider;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeycloakIdentityProviderImpl implements IdentityProvider {
	
	private final KeycloakAdminTokenClient adminTokenClient;
	private final String realm; 
	private final String clientId;
	private final String clientSecret;
	
	private final Logger logger = LoggerFactory.getLogger(KeycloakIdentityProviderImpl.class);

	public KeycloakIdentityProviderImpl(
			@RestClient KeycloakAdminTokenClient adminTokenClient, 
			@ConfigProperty(name = "keycloak.realm") String realm, 
			@ConfigProperty(name = "keycloak.client-id") String clientId, 
			@ConfigProperty(name = "keycloak.client-secret") String clientSecret) {
		this.adminTokenClient = adminTokenClient;
		this.realm = realm;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	@CacheResult(cacheName = "keycloak-token")
	@Override
	public KeycloakTokenResponse generateToken() {
		logger.info("Generating token for client {}", clientId);
		logger.info("Realm: {}, client_credentials, clientId: {}, clientSecret: {}", realm, clientId, clientSecret);
		KeycloakTokenResponse keycloakTokenResponse = this.adminTokenClient
				.generateAdminToken(realm, "client_credentials", clientId, clientSecret);
		return keycloakTokenResponse;
	}

}
