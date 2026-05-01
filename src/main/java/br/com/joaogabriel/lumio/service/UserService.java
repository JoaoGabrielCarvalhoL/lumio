package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.UserCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.ProvisioningResponse;

public interface UserService {
	
	ProvisioningResponse save(final UserCreateRequest userCreateRequest);

}
