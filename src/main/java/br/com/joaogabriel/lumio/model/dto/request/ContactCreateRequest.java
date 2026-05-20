package br.com.joaogabriel.lumio.model.dto.request;

import br.com.joaogabriel.lumio.model.enumerations.ContactType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactCreateRequest(
        @NotNull(message = "The field type cannot be null.")
        ContactType type,
        @NotBlank(message = "The field contact cannot be empty or blank.")
        String contact,
        Boolean isPrimary
) {
}
