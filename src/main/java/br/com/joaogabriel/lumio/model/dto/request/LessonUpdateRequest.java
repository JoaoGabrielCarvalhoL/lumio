package br.com.joaogabriel.lumio.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record LessonUpdateRequest(
        @NotNull(message = "The field id cannot be null.")
        UUID id,
        @NotBlank(message = "The field name cannot be empty or blank.")
        String name,

        @NotNull(message = "The field order cannot be null.")
        @Positive
        Integer order
) {
}
