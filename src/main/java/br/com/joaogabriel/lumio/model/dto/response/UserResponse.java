package br.com.joaogabriel.lumio.model.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
		UUID id, 
		String username,
		String email,
		String firstName, 
		String lastName) {

}
