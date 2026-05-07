package br.com.joaogabriel.lumio.resource;

import br.com.joaogabriel.lumio.annotation.CheckResourceOwner;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/v1/zip")
@Tag(name = "ZipCode Management", description = "Endpoint to retrieve address by zipcode.")
public interface ZipCodeResource {

    @GET
    @Operation(summary = "Fetch Address by Zipcode", description = "Endpoint to retrieve address by zipcode.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", name = "OK", description = "User created successfully."),
            @APIResponse(responseCode = "400", name = "NOT_FOUND", description = "Invalid input data."),
            @APIResponse(responseCode = "401", name = "Internal Server Error", description = "Internal Server Error."),
    })
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{zipCode}")
    Response getAddressByZipCode(@PathParam("zipCode") String zipCode);
}
