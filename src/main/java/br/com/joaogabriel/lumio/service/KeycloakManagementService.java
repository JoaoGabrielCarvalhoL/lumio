package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.client.dto.request.KeycloakCreateUserRequest;
import br.com.joaogabriel.lumio.model.UserProvisioningResult;

public interface KeycloakManagementService {

	UserProvisioningResult createUser(KeycloakCreateUserRequest createUserRequest);
}
