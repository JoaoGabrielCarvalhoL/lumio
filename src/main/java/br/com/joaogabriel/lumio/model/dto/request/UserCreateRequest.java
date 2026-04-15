package br.com.joaogabriel.lumio.model.dto.request;

import br.com.joaogabriel.lumio.annotation.UniqueEmail;
import br.com.joaogabriel.lumio.annotation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
		@NotBlank(message = "The field username cannot be empty or blank.")
		@UniqueUsername
		String username,
		
		@NotBlank(message = "The field email cannot be empty or blank.")
		@UniqueEmail
		@Email
		String email,
		
		@NotBlank(message = "The field firstName cannot be empty or blank.")
		String firstName,
		
		@NotBlank(message = "The field lastName cannot be empty or blank.")
		String lastName,
		
		@NotBlank(message = "The field password cannot be empty or blank.")
		String password) {

}
