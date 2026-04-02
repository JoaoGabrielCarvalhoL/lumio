package br.com.joaogabriel.lumio.exception.handler;

import java.time.LocalDateTime;
import java.util.Collections;

import br.com.joaogabriel.lumio.exception.response.ExceptionResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import software.amazon.awssdk.core.exception.SdkClientException;

@Provider
public class SdkClientExceptionHandler implements ExceptionMapper<SdkClientException> {

	@Override
	public Response toResponse(SdkClientException exception) {
		return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(new ExceptionResponse(
                        "Service Unavailable",
                        exception.getMessage(),
                        Response.Status.SERVICE_UNAVAILABLE.getStatusCode(), 
                        LocalDateTime.now(), 
                        Collections.emptyMap()))
                .build();
	}

}
