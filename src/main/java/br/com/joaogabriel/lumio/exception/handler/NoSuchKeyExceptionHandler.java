package br.com.joaogabriel.lumio.exception.handler;

import java.time.LocalDateTime;
import java.util.Collections;

import br.com.joaogabriel.lumio.exception.response.ExceptionResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Provider
public class NoSuchKeyExceptionHandler implements ExceptionMapper<NoSuchKeyException> {

	@Override
	public Response toResponse(NoSuchKeyException exception) {
		return Response.status(Response.Status.NOT_FOUND)
                .entity(new ExceptionResponse(
                        "Not Found",
                        exception.getMessage(),
                        Response.Status.NOT_FOUND.getStatusCode(), 
                        LocalDateTime.now(), 
                        Collections.emptyMap()))
                .build();
	}
	
	

}
