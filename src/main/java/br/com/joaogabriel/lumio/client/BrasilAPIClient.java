package br.com.joaogabriel.lumio.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.joaogabriel.lumio.client.dto.response.BrasilAddressResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@RegisterRestClient(configKey = "brasilapi")
@Path("/api/cep/v1")
public interface BrasilAPIClient {
	
	@GET
    @Path("/{cep}")
    BrasilAddressResponse getAddress(@PathParam("cep") String cep);

}
