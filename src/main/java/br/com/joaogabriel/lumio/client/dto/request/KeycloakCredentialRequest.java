package br.com.joaogabriel.lumio.client.dto.request;

public record KeycloakCredentialRequest(
		String type, 
		String value, 
		Boolean temporary) {

}
