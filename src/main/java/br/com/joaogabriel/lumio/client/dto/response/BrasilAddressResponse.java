package br.com.joaogabriel.lumio.client.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BrasilAddressResponse(
		String cep,
	    String state,
	    String city,
	    String neighborhood,
	    String street,
	    String service) {

}
