package br.com.joaogabriel.lumio.exception.handler;

import java.time.LocalDateTime;
import java.util.Collections;

import br.com.joaogabriel.lumio.exception.InvalidRequestException;
import br.com.joaogabriel.lumio.exception.response.ExceptionResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidRequestExceptionHandler implements ExceptionMapper<InvalidRequestException> {

	@Override
	public Response toResponse(InvalidRequestException exception) {
		return Response.status(422)
                .entity(new ExceptionResponse(
                        "Unprocessable Content",
                        exception.getMessage(),
                        422, 
                        LocalDateTime.now(), 
                        Collections.emptyMap()))
                .build();
	}

}
