package br.com.joaogabriel.lumio.model.dto.response;

import br.com.joaogabriel.lumio.model.enumerations.ContactType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContactResponse(
        UUID id,
        ContactType type,
        String contact
) {
}
