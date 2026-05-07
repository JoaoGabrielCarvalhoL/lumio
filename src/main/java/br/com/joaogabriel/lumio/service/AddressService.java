package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.AddressCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.AddressResponse;

import java.util.UUID;

public interface AddressService {

    AddressResponse save(final AddressCreateRequest address, final UUID userId);
}
