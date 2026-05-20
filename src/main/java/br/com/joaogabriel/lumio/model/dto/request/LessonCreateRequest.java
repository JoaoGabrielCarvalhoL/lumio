package br.com.joaogabriel.lumio.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LessonCreateRequest(

        @NotBlank(message = "The field name cannot be empty or blank.")
        String name,

        @NotNull(message = "The field order cannot be null.")
        @Positive(message = "The field order cannot be negative.")
        Integer order
) {
}
