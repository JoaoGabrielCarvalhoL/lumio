package br.com.joaogabriel.lumio.exception.handler;

import java.time.LocalDateTime;
import java.util.Collections;

import br.com.joaogabriel.lumio.exception.response.ExceptionResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@Provider
public class NoSuchBucketExceptionHandler implements ExceptionMapper<NoSuchBucketException> {

	@Override
	public Response toResponse(NoSuchBucketException exception) {
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
