package br.com.joaogabriel.lumio.client;

import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.time.temporal.ChronoUnit;

@RegisterRestClient(configKey = "brasilapi")
@Path("/api/cep/v1")
public interface BrasilAPIClient {
	
	@GET
    @Path("/{cep}")
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 10, delayUnit = ChronoUnit.SECONDS, successThreshold = 2)
    @Retry(maxRetries =  2, delay = 1, delayUnit = ChronoUnit.SECONDS)
    @Timeout(value = 1, unit = ChronoUnit.SECONDS)
    @Bulkhead(value = 3)
    Response getAddress(@PathParam("cep") String cep);

}
