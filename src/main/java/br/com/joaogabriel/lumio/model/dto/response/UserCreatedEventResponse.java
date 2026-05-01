package br.com.joaogabriel.lumio.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserCreatedEventResponse(
        UUID id,
        String email,
        String firstName,
        String username
) {
}
