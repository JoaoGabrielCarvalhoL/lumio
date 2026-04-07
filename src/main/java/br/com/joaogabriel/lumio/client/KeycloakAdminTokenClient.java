package br.com.joaogabriel.lumio.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.joaogabriel.lumio.client.dto.response.KeycloakTokenResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "keycloak-token")
@Path("/realms/{realm}/protocol/openid-connect")
public interface KeycloakAdminTokenClient {
	
	@POST
    @Path("/token")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    KeycloakTokenResponse generateAdminToken(@PathParam("realm") String realm, @FormParam("grant_type") String grantType,
      @FormParam("client_id") String clientId, @FormParam("client_secret") String clientSecret);

}
