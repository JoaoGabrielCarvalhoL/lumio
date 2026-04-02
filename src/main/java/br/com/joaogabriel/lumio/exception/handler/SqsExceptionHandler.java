package br.com.joaogabriel.lumio.exception.handler;

import java.time.LocalDateTime;
import java.util.Collections;

import br.com.joaogabriel.lumio.exception.response.ExceptionResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Provider
public class SqsExceptionHandler implements ExceptionMapper<SqsException> {

	@Override
	public Response toResponse(SqsException exception) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ExceptionResponse(
                        "Internal Server Error",
                        exception.getMessage(),
                        Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), 
                        LocalDateTime.now(), 
                        Collections.emptyMap()))
                .build();
	}

}
