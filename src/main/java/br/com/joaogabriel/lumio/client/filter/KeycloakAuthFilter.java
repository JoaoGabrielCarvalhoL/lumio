package br.com.joaogabriel.lumio.client.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.joaogabriel.lumio.client.dto.response.KeycloakTokenResponse;
import br.com.joaogabriel.lumio.service.IdentityProvider;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class KeycloakAuthFilter implements ClientRequestFilter {
	
	private final IdentityProvider identityProvider;
	private final static Logger logger = LoggerFactory.getLogger(KeycloakAuthFilter.class);
	
	public KeycloakAuthFilter(IdentityProvider identityProvider) {
		this.identityProvider = identityProvider;
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		if (!requestContext.getUri().getPath().contains("/token")) {
			KeycloakTokenResponse response = identityProvider.generateToken();
			logger.info("Using token: {}", response.accessToken()); 
			logger.info("Setting Authorization header for request to {}", requestContext.getUri());
			
			requestContext.getHeaders()
				.add("Authorization", "Bearer " + response.accessToken());
	    }
		
	}

}
