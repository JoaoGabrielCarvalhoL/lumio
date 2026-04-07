package br.com.joaogabriel.lumio.client.dto.request;

public record KeycloakResetPasswordRequest(
		String type, 
		String value, 
		Boolean temporary) {

}
