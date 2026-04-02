package br.com.joaogabriel.lumio.exception.handler;

import java.time.LocalDateTime;
import java.util.Collections;

import br.com.joaogabriel.lumio.exception.OperationNotAllowedException;
import br.com.joaogabriel.lumio.exception.response.ExceptionResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class OperationNotAllowedExceptionHandler implements ExceptionMapper<OperationNotAllowedException> {

	@Override
	public Response toResponse(OperationNotAllowedException exception) {
		return Response.status(Response.Status.CONFLICT)
                .entity(new ExceptionResponse(
                        "Conflict",
                        exception.getMessage(),
                        Response.Status.CONFLICT.getStatusCode(), 
                        LocalDateTime.now(), 
                        Collections.emptyMap()))
                .build();
	}

}
