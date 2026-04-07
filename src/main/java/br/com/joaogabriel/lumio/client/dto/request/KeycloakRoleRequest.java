package br.com.joaogabriel.lumio.client.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KeycloakRoleRequest(
		String id, 
		String name, 
		String description, 
		Boolean composite, 
		Boolean clientRole, 
		String containerId) {

}
