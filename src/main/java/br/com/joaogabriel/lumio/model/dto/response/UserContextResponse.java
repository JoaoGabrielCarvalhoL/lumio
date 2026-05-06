package br.com.joaogabriel.lumio.model.dto.response;

public record UserContextResponse(
        String ip,
        String rawAgent
) {
}
