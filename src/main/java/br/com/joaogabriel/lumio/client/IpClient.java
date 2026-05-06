package br.com.joaogabriel.lumio.client;

import br.com.joaogabriel.lumio.client.dto.response.IpResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "ip")
@Path("/json")
public interface IpClient {

    @GET
    @Path("/{ip}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getDetails(@PathParam("ip") String ip);
}
