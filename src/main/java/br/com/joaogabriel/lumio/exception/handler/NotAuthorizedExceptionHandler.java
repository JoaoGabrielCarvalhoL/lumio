package br.com.joaogabriel.lumio.exception.handler;

import java.time.LocalDateTime;
import java.util.Collections;

import br.com.joaogabriel.lumio.exception.response.ExceptionResponse;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotAuthorizedExceptionHandler implements ExceptionMapper<NotAuthorizedException> {

	@Override
	public Response toResponse(NotAuthorizedException exception) {
		return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ExceptionResponse(
                        "Unauthorized",
                        exception.getMessage(),
                        Response.Status.UNAUTHORIZED.getStatusCode(),
                        LocalDateTime.now(),
                        Collections.emptyMap()))
                .build();
	}

}
