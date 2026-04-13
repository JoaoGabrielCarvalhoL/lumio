package br.com.joaogabriel.lumio.resource;

import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.joaogabriel.lumio.client.KeycloakAdminManagementClient;
import br.com.joaogabriel.lumio.client.dto.request.KeycloakCreateUserRequest;
import br.com.joaogabriel.lumio.client.dto.request.KeycloakCredentialRequest;
import br.com.joaogabriel.lumio.client.dto.response.KeycloakTokenResponse;
import br.com.joaogabriel.lumio.service.IdentityProvider;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("test")
public class TestController {
	
	private final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Inject
    IdentityProvider identityProvider;
    
    @Inject
    @RestClient
    KeycloakAdminManagementClient keycloakAdminManagementClient;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public KeycloakTokenResponse generateToken() {
        logger.info("Endpoint /test-keycloak called, generating token...");
        KeycloakTokenResponse token = identityProvider.generateToken();
        logger.info("Token generated successfully!");
        return token;
    }
    
    @POST
    public Response createUser() {
        KeycloakCredentialRequest password = new KeycloakCredentialRequest("password", "123456789", false);

        KeycloakCreateUserRequest userRequest = new KeycloakCreateUserRequest(
                "testeuser",
                "testeuser@example.com",
                true,
                true,
                "Teste",
                "User",
                List.of(),
                List.of(password),
                Map.of()
            );

        Response response = keycloakAdminManagementClient.createUser("lumio", userRequest);

        if (response.getStatus() == 201) {
        		response.getLocation();
            return Response.ok(response.getLocation()).build();
        } else {
            String body = response.readEntity(String.class);
            return Response.status(response.getStatus())
                           .entity("Failed to create user: " + body)
                           .build();
        }
    }

}
