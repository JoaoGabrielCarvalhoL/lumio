package br.com.joaogabriel.lumio.client.dto.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeycloakUserResponse(
		String id, 
		String username, 
		String email, 
		Boolean enabled, 
		Boolean emailVerified, 
		String firstName, 
		String lastName, 
		List<String> requiredActions,
		List<KeycloakCredentialResponse> credentials, 
		Map<String, List<String>> attributes,
		KeycloakAccessResponse access, 
		List<KeycloakFederatedIdentityResponse> federatedIdentities, 
		String notBefore, 
		String serviceAccountClientId, 
		List<String> disableableCredentialTypes, 
		Boolean totp) {

}
