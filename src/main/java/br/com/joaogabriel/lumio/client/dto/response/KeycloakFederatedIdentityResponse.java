package br.com.joaogabriel.lumio.client.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeycloakFederatedIdentityResponse(
		String identityProvider, 
		String userId, 
		String userName) {

}
