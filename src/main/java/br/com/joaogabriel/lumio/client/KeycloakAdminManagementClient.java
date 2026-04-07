package br.com.joaogabriel.lumio.client;

import java.util.List;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import br.com.joaogabriel.lumio.client.dto.request.KeycloakCreateUserRequest;
import br.com.joaogabriel.lumio.client.dto.request.KeycloakResetPasswordRequest;
import br.com.joaogabriel.lumio.client.dto.request.KeycloakUserAction;
import br.com.joaogabriel.lumio.client.dto.response.KeycloakClientResponse;
import br.com.joaogabriel.lumio.client.dto.response.KeycloakRoleResponse;
import br.com.joaogabriel.lumio.client.dto.response.KeycloakUserResponse;
import br.com.joaogabriel.lumio.client.filter.KeycloakAuthFilter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RegisterRestClient(configKey = "keycloak-admin")
@RegisterProvider(KeycloakAuthFilter.class)
@Path("/admin/realms/{realm}")
public interface KeycloakAdminManagementClient {

    @POST
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    Response createUser(@PathParam("realm") String realm, KeycloakCreateUserRequest body);

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    List<KeycloakUserResponse> getUsers(@PathParam("realm") String realm,
    		@QueryParam("username") String username, @QueryParam("email") String email,
        @QueryParam("first") Integer first, @QueryParam("max") Integer max);

    @GET
    @Path("/users/count")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUsersCount(@PathParam("realm") String realm, @QueryParam("username") String username,
        @QueryParam("email") String email);

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    KeycloakUserResponse getUserById(@PathParam("realm") String realm, @PathParam("id") String id);

    @PUT
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateUser(@PathParam("realm") String realm,
    		@PathParam("id") String id, KeycloakCreateUserRequest body);

    @DELETE
    @Path("/users/{id}")
    Response deleteUser(@PathParam("realm") String realm, @PathParam("id") String id);

    @PUT
    @Path("/users/{id}/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    Response resetPassword(@PathParam("realm") String realm, @PathParam("id") String id,
        KeycloakResetPasswordRequest body);

    @PUT
    @Path("/users/{id}/execute-actions-email")
    @Consumes(MediaType.APPLICATION_JSON)
    Response executeActionsEmail(@PathParam("realm") String realm, @PathParam("id") String id,
        List<KeycloakUserAction> actions);

    @POST
    @Path("/users/{id}/logout")
    Response logoutUser(@PathParam("realm") String realm, @PathParam("id") String id);

    @POST
    @Path("/users/{id}/role-mappings/realm")
    @Consumes(MediaType.APPLICATION_JSON)
    Response assignRealmRoles(@PathParam("realm") String realm, @PathParam("id") String id,
        List<KeycloakRoleResponse> roles);

    @GET
    @Path("/users/{id}/role-mappings")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUserRoleMappings(@PathParam("realm") String realm, @PathParam("id") String id);

    @GET
    @Path("/users/{id}/groups")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUserGroups(@PathParam("realm") String realm, @PathParam("id") String id);

    @PUT
    @Path("/users/{id}/groups/{groupId}")
    Response addUserToGroup(@PathParam("realm") String realm, @PathParam("id") String id,
      @PathParam("groupId") String groupId);

    @DELETE
    @Path("/users/{id}/groups/{groupId}")
    Response removeUserFromGroup(@PathParam("realm") String realm, @PathParam("id") String id,
      @PathParam("groupId") String groupId);

    @GET
    @Path("/users/{id}/sessions")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUserSessions(@PathParam("realm") String realm, @PathParam("id") String id);
    
    @GET
    @Path("/clients")
    @Produces(MediaType.APPLICATION_JSON)
    List<KeycloakClientResponse> getClients(@PathParam("realm") String realm);

    @GET
    @Path("/clients/{clientId}/roles")
    @Produces(MediaType.APPLICATION_JSON)
    List<KeycloakRoleResponse> getClientRoles(@PathParam("realm") String realm, @PathParam("clientId") String clientId);

    @POST
    @Path("/clients/{clientId}/service-account-user/role-mappings/clients/{roleClientId}")
    @Consumes(MediaType.APPLICATION_JSON)
    Response assignClientRolesToServiceAccount(@PathParam("realm") String realm, @PathParam("clientId") String clientId,
      @PathParam("roleClientId") String roleClientId, List<KeycloakRoleResponse> roles);
    
    @GET
    @Path("/clients/{clientId}/service-account-user")
    @Produces(MediaType.APPLICATION_JSON)
    KeycloakUserResponse getServiceAccountUser(@PathParam("realm") String realm, @PathParam("clientId") String clientId);
}