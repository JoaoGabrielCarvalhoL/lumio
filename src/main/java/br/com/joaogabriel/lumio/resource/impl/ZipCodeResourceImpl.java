package br.com.joaogabriel.lumio.resource.impl;

import br.com.joaogabriel.lumio.resource.ZipCodeResource;
import br.com.joaogabriel.lumio.service.ZipCodeService;
import jakarta.ws.rs.core.Response;

public class ZipCodeResourceImpl implements ZipCodeResource {

    private final ZipCodeService zipCodeService;

    public ZipCodeResourceImpl(ZipCodeService zipCodeService) {
        this.zipCodeService = zipCodeService;
    }

    @Override
    public Response getAddressByZipCode(String zipCode) {
        return Response.status(Response.Status.OK)
                .entity(this.zipCodeService.getAddressByCep(zipCode))
                .build();
    }
}
