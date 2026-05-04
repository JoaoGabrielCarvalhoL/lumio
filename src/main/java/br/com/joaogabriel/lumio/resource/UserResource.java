package br.com.joaogabriel.lumio.resource;

import br.com.joaogabriel.lumio.annotation.CheckResourceOwner;
import br.com.joaogabriel.lumio.model.dto.request.UserCreateRequest;
import br.com.joaogabriel.lumio.model.dto.request.UserPasswordResetRequest;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.UUID;

@Path("/v1/users")
@Tag(name = "User Management", description = "Operations related to user provisioning and lifecycle.")
public interface UserResource {

	@POST
	@Operation(summary = "Create a new user", description = "Provisions a user in Keycloak and triggers the welcome flow.")
	@APIResponses(value = {
			@APIResponse(responseCode = "201", name = "CREATED", description = "User created successfully."),
			@APIResponse(responseCode = "400", name = "BAD_REQUEST", description = "Invalid input data."),
			@APIResponse(responseCode = "409", name = "CONFLICT", description = "User already exists.")
	})
	@Produces(MediaType.APPLICATION_JSON)
	Response create(@Valid UserCreateRequest request);

	@PATCH
	@Path("/{id}/password")
	@Operation(summary = "Reset user password", description = "Updates the user's password in the identity provider.")
	@APIResponse(responseCode = "204", description = "Password updated")
	@Produces(MediaType.APPLICATION_JSON)
	Response resetPassword(@PathParam("id") UUID id, @Valid UserPasswordResetRequest request);

	

}
