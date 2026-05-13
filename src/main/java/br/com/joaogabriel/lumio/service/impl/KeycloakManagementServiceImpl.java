package br.com.joaogabriel.lumio.service.impl;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import br.com.joaogabriel.lumio.client.dto.response.KeycloakRoleResponse;
import br.com.joaogabriel.lumio.exception.KeycloakOperationException;
import br.com.joaogabriel.lumio.exception.KeycloakRetryableException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.joaogabriel.lumio.client.KeycloakAdminManagementClient;
import br.com.joaogabriel.lumio.client.dto.request.KeycloakCreateUserRequest;
import br.com.joaogabriel.lumio.exception.KeycloakException;
import br.com.joaogabriel.lumio.exception.mapper.KeycloakErrorMapper;
import br.com.joaogabriel.lumio.model.dto.response.UserProvisioningResult;
import br.com.joaogabriel.lumio.model.enumerations.ProvisioningStatus;
import br.com.joaogabriel.lumio.service.KeycloakManagementService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class KeycloakManagementServiceImpl implements KeycloakManagementService {
	
	private static final Logger LOG = LoggerFactory.getLogger(KeycloakManagementServiceImpl.class);
	
	private final KeycloakAdminManagementClient keycloakAdminManagementClient;
	
	private final String realm;

	public KeycloakManagementServiceImpl(@RestClient KeycloakAdminManagementClient keycloakAdminManagementClient, 
			@ConfigProperty(name = "keycloak.realm") String realm) {
		this.keycloakAdminManagementClient = keycloakAdminManagementClient;
		this.realm = realm;
	}

    @CircuitBreaker(requestVolumeThreshold = 10, failureRatio = 0.5, delay = 5000)
	@Override
	public UserProvisioningResult createUser(KeycloakCreateUserRequest createUserRequest) {
		LOG.info("Creating user {}, into keycloak.", createUserRequest.username());
		try (Response response = keycloakAdminManagementClient.createUser(this.realm, createUserRequest)) {
			
			if (response.getStatus() >= 300) {
				KeycloakErrorMapper.KeycloakErrorContext context =
						KeycloakErrorMapper.KeycloakErrorContext.from(response.getStatus(), "Failed to create user", null);
				if (context.retryable()) {
					throw new KeycloakRetryableException(context);
				}
				throw new KeycloakOperationException(context);
			}

			LOG.info("User created successfully. Username: {}", createUserRequest.username());
			String keycloakId = this.extractIdFromHeaderLocation(response);
			this.assignDefaultRole(keycloakId);

			return new UserProvisioningResult(keycloakId,
					ProvisioningStatus.CREATED, null);
		}
	}

	/**
	 * Assigns the default role 'USER' to an existing user in Keycloak.
	 *
	 * @param keycloakId The user's UUID returned by Keycloak.
	 * @throws KeycloakOperationException If there is an error in the communication with the Identity Provider.
	 */
	private void assignDefaultRole(String keycloakId) {
		Objects.requireNonNull(keycloakId, "keycloakId cannot be null");
		LOG.info("Assigning default role to user {}.", keycloakId);

		try {
			KeycloakRoleResponse role = this.keycloakAdminManagementClient.getRoleByName(this.realm, "USER");
			this.keycloakAdminManagementClient.assignRealmRoles(this.realm, keycloakId, List.of(role));
			LOG.info("Default role assigned to user {}.", keycloakId);
		} catch (KeycloakOperationException e) {
			LOG.error("Failed to assign default role to user {}. Reason: {}", keycloakId, e.getMessage());
			throw e;
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
