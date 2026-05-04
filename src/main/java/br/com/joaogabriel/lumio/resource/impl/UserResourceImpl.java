package br.com.joaogabriel.lumio.resource.impl;

import br.com.joaogabriel.lumio.annotation.CheckResourceOwner;
import br.com.joaogabriel.lumio.model.dto.request.UserCreateRequest;
import br.com.joaogabriel.lumio.model.dto.request.UserPasswordResetRequest;
import br.com.joaogabriel.lumio.resource.UserResource;
import br.com.joaogabriel.lumio.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.UUID;

public class UserResourceImpl implements UserResource {

    private final UserService userService;
    private final JsonWebToken jwt;

    public UserResourceImpl(UserService userService, JsonWebToken jwt) {
        this.userService = userService;
        this.jwt = jwt;
    }

    @Override
    @PermitAll
    public Response create(UserCreateRequest request) {
        return Response.status(Response.Status.ACCEPTED)
                .entity(this.userService.save(request))
                .build();
    }

    @Override
    @CheckResourceOwner
    @RolesAllowed({ "USER", "ADMIN"})
    public Response resetPassword(UUID id, UserPasswordResetRequest request) {
        return Response.status(Response.Status.ACCEPTED).build();
    }
}
