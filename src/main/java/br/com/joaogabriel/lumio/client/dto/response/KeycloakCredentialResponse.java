package br.com.joaogabriel.lumio.client.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeycloakCredentialResponse(
		String type, 
		String value, 
		Boolean temporary, 
		Long createdDate, 
		Long expiration) {

}
