package br.com.joaogabriel.lumio.client.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeycloakClientResponse(
		String id, 
		String clientId, 
		String name, 
		Boolean serviceAccountsEnabled) {

}
