package br.com.joaogabriel.lumio.resource;

import br.com.joaogabriel.lumio.annotation.CheckResourceOwner;
import br.com.joaogabriel.lumio.model.dto.request.AddressCreateRequest;
import br.com.joaogabriel.lumio.model.dto.request.UserCreateRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Path("/v1/users")
@Tag(name = "Address Management", description = "Operations related to user address.")
public interface AddressResource {

    @POST
    @Operation(summary = "Create a new address", description = "Provisions a user in Keycloak and triggers the welcome flow.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", name = "CREATED", description = "User created successfully."),
            @APIResponse(responseCode = "400", name = "BAD_REQUEST", description = "Invalid input data."),
            @APIResponse(responseCode = "401", name = "UNAUTHORIZED", description = "Unauthorized."),
            @APIResponse(responseCode = "409", name = "Forbidden", description = "Forbidden."),
    })
    @Produces(MediaType.APPLICATION_JSON)
    @CheckResourceOwner
    @Path("/{userId}/address")
    Response create(@PathParam("{userId}") UUID userId, @Valid AddressCreateRequest request);
}
