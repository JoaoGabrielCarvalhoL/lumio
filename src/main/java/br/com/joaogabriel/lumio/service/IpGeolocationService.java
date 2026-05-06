package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.client.dto.response.IpResponse;
import br.com.joaogabriel.lumio.model.entity.UserProvisioning;

import java.util.Optional;

public interface IpGeolocationService {

    Optional<IpResponse> getDetails(final UserProvisioning provisioning);
}
