package br.com.joaogabriel.lumio.client.dto.request;

import java.util.List;

public record KeycloakExecuteActionsRequest(
		List<KeycloakRoleRequest> roles) {

}
