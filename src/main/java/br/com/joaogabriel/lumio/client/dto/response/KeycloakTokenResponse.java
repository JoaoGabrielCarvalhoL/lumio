package br.com.joaogabriel.lumio.client.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeycloakTokenResponse(
		@JsonProperty("access_token")
		String accessToken,
		
		@JsonProperty("expires_in")
		Long expiresIn, 
		
		@JsonProperty("token_type")
		String tokenType, 
		
		@JsonProperty("refresh_expires_in")
		Long refreshExpiresIn, 
		
		String scope) {

}
