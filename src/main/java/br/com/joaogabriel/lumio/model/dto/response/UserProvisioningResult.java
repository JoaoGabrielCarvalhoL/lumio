package br.com.joaogabriel.lumio.model.dto.response;

import br.com.joaogabriel.lumio.model.enumerations.ProvisioningStatus;

public record UserProvisioningResult(
		String keycloakId, 
		ProvisioningStatus status, 
		String errorMessage) {

	public boolean isActive() {
		return this.status == ProvisioningStatus.CREATED;
	}
	
}
