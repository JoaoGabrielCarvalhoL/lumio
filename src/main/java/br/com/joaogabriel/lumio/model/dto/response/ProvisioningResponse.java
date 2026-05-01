package br.com.joaogabriel.lumio.model.dto.response;

import java.util.UUID;

import br.com.joaogabriel.lumio.model.enumerations.ProvisioningStatus;

public record ProvisioningResponse(
		UUID id, 
		ProvisioningStatus status) {

}
