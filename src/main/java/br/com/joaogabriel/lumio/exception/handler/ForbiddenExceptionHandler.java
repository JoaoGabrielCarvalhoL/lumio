package br.com.joaogabriel.lumio.exception.handler;

import java.time.LocalDateTime;
import java.util.Collections;

import br.com.joaogabriel.lumio.exception.response.ExceptionResponse;
import io.quarkus.security.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionHandler implements ExceptionMapper<ForbiddenException> {

	@Override
	public Response toResponse(ForbiddenException exception) {
		return Response.status(Response.Status.FORBIDDEN)
                .entity(new ExceptionResponse(
                        "Forbidden",
                        exception.getMessage(),
                        Response.Status.FORBIDDEN.getStatusCode(),
                        LocalDateTime.now(),
                        Collections.emptyMap()))
                .build();
	}

}
