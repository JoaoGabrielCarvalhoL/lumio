package br.com.joaogabriel.lumio.resource.impl;

import br.com.joaogabriel.lumio.model.dto.request.AddressCreateRequest;
import br.com.joaogabriel.lumio.resource.AddressResource;
import br.com.joaogabriel.lumio.service.AddressService;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

public class AddressResourceImpl implements AddressResource {

    private final AddressService addressService;

    public AddressResourceImpl(AddressService addressService) {
        this.addressService = addressService;
    }

    @Override
    public Response create(UUID userId, AddressCreateRequest request) {
        return Response.status(Response.Status.CREATED)
                .entity(this.addressService.save(request, userId))
                .build();
    }
}
