package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.client.dto.response.KeycloakTokenResponse;

public interface IdentityProvider {

	KeycloakTokenResponse generateToken();
}
