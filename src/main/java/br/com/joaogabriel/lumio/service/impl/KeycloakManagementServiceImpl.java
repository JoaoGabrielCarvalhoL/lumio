package br.com.joaogabriel.lumio.service.impl;

import java.net.URI;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.joaogabriel.lumio.client.KeycloakAdminManagementClient;
import br.com.joaogabriel.lumio.client.dto.request.KeycloakCreateUserRequest;
import br.com.joaogabriel.lumio.exception.KeycloakException;
import br.com.joaogabriel.lumio.exception.mapper.KeycloakErrorMapper;
import br.com.joaogabriel.lumio.model.UserProvisioningResult;
import br.com.joaogabriel.lumio.model.enumerations.ProvisioningStatus;
import br.com.joaogabriel.lumio.service.KeycloakManagementService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class KeycloakManagementServiceImpl implements KeycloakManagementService {
	
	private static final Logger logger = LoggerFactory.getLogger(KeycloakManagementServiceImpl.class);
	
	private final KeycloakAdminManagementClient keycloakAdminManagementClient;
	
	private final String realm;

	public KeycloakManagementServiceImpl(@RestClient KeycloakAdminManagementClient keycloakAdminManagementClient, 
			@ConfigProperty(name = "keycloak.realm") String realm) {
		this.keycloakAdminManagementClient = keycloakAdminManagementClient;
		this.realm = realm;
	}
	
	@Retry(maxRetries = 3, delay = 200)
    @Timeout(3000)
    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5, delay = 5000)
	@Override
	public UserProvisioningResult createUser(KeycloakCreateUserRequest createUserRequest) {
		logger.info("Creating user {}, into keycloak.", createUserRequest.username());
		try (Response response = keycloakAdminManagementClient.createUser(this.realm, createUserRequest)) {
			
			if (response.getStatus() >= 300) {
				throw KeycloakErrorMapper.fromStatus(response.getStatus(), "Failed to create user", null);				
			}
		
			logger.info("User created successfully. Username: {}", createUserRequest.username());
			return new UserProvisioningResult(this.extractIdFromHeaderLocation(response), 
					ProvisioningStatus.CREATED, null);
		}
	}

	private String extractIdFromHeaderLocation(Response response) {
		URI location = Optional.ofNullable(response.getLocation())
	            .orElseThrow(() -> new KeycloakException("Missing Location header in Keycloak Response."));

	    String path = Optional.ofNullable(location.getPath())
	            .orElseThrow(() -> new KeycloakException("Location header has no path: " + location));

	    int lastSlash = path.lastIndexOf('/');

	    if (lastSlash == -1 || lastSlash == path.length() - 1) {
	        throw new KeycloakException("Unable to extract ID from Location header: " + location);
	    }

	    return path.substring(lastSlash + 1);
	}
	
	

}
