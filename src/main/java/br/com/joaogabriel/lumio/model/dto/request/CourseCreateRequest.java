package br.com.joaogabriel.lumio.model.dto.request;

import br.com.joaogabriel.lumio.model.enumerations.CourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseCreateRequest(
        @NotBlank(message = "The field name cannot be empty or blank.")
        String name,

        @NotBlank(message = "The field content cannot be empty or blank.")
        String content,

        @NotBlank(message = "The field description cannot be empty or blank.")
        String description,

        @NotBlank(message = "The field language cannot be empty or blank.")
        String language,

        @NotNull(message = "The field level cannot be null.")
        CourseLevel level
) {
}
