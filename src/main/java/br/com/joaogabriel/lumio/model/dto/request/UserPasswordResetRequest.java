package br.com.joaogabriel.lumio.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserPasswordResetRequest(
        @NotBlank
        String password
) {
}
