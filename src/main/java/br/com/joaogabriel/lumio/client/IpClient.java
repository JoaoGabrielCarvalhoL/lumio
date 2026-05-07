package br.com.joaogabriel.lumio.client;

import br.com.joaogabriel.lumio.client.dto.response.IpResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

@RegisterRestClient(configKey = "ip")
@Path("/json")
public interface IpClient {

    @GET
    @Path("/{ip}")
    @Produces(MediaType.APPLICATION_JSON)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 10, delayUnit = ChronoUnit.SECONDS, successThreshold = 2)
    @Retry(maxRetries =  2, delay = 5, delayUnit = ChronoUnit.SECONDS)
    @Timeout(value = 1, unit = ChronoUnit.SECONDS)
    @Bulkhead(value = 3)
    Response getDetails(@PathParam("ip") String ip);
}
