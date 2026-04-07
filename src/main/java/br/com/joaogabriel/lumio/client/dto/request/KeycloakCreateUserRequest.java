package br.com.joaogabriel.lumio.client.dto.request;

import java.util.List;
import java.util.Map;

public record KeycloakCreateUserRequest(
		String username,
	    String email,
	    Boolean enabled,
	    Boolean emailVerified,
	    String firstName,
	    String lastName,
	    List<String> requiredActions,
	    List<KeycloakCredentialRequest> credentials,
	    Map<String, List<String>> attributes) {

}
