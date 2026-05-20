package br.com.joaogabriel.lumio.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
        @NotBlank(message = "The field name cannot be empty or blank.")
        String name,
        @NotBlank(message = "The field description cannot be empty or blank.")
        String description
) {
}
