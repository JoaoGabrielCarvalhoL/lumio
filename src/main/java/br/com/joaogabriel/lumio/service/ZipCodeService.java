package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.client.dto.response.BrasilAddressResponse;

public interface ZipCodeService {

    BrasilAddressResponse getAddressByCep(final String cep);
}
