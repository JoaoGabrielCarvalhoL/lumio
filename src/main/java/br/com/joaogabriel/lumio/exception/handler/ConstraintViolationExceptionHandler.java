package br.com.joaogabriel.lumio.exception.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import br.com.joaogabriel.lumio.exception.response.ExceptionResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

	@Override
	public Response toResponse(ConstraintViolationException exception) {
		Map<String, String> errors = new HashMap<>();
		
		for (ConstraintViolation<?> constraintViolations : exception.getConstraintViolations()) {
			String field = constraintViolations.getPropertyPath().toString();
			errors.put(field, constraintViolations.getMessage());
		}
		
		return Response.status(422)
                .entity(new ExceptionResponse(
                        "Unprocessable Content",
                        exception.getMessage(),
                        422, 
                        LocalDateTime.now(), 
                        errors))
                .build();
	}

}
