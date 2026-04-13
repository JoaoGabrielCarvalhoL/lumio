package br.com.joaogabriel.lumio.model;

import br.com.joaogabriel.lumio.model.enumerations.UserProvisioningStatus;

public record UserProvisioningResult(
		String keycloakId, 
		UserProvisioningStatus status) {

	public boolean isActive() {
		return this.status == UserProvisioningStatus.CREATED;
	}
	
	public boolean isPending() {
		return this.status == UserProvisioningStatus.PENDING_QUEUE;
	}
}
