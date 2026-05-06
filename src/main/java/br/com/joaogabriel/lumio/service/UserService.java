package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.UserCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.ProvisioningResponse;
import br.com.joaogabriel.lumio.model.dto.response.UserContextResponse;

public interface UserService {
	
	ProvisioningResponse save(final UserCreateRequest userCreateRequest,
							  final UserContextResponse  userContext);

}
