package br.com.joaogabriel.lumio.resource.impl;

import br.com.joaogabriel.lumio.annotation.CheckResourceOwner;
import br.com.joaogabriel.lumio.model.dto.request.UserCreateRequest;
import br.com.joaogabriel.lumio.model.dto.request.UserPasswordResetRequest;
import br.com.joaogabriel.lumio.model.dto.response.UserContextResponse;
import br.com.joaogabriel.lumio.resource.UserResource;
import br.com.joaogabriel.lumio.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
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
    public Response create(UserCreateRequest request,
                           @Context HttpHeaders headers) {
        return Response.status(Response.Status.ACCEPTED)
                .entity(this.userService.save(request, extractUserContext(headers)))
                .build();
    }

    @Override
    @CheckResourceOwner
    @RolesAllowed({ "USER", "ADMIN"})
    public Response resetPassword(UUID id, UserPasswordResetRequest request) {
        return Response.status(Response.Status.ACCEPTED).build();
    }

    private UserContextResponse extractUserContext(HttpHeaders headers) {
        String[] IP_HEADERS = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR",
                "X-Real-IP"
        };
        String ip = null;

        for (String header : IP_HEADERS) {
            String value = headers.getHeaderString(header);
            if (value != null && !value.isEmpty() && !"unknown".equalsIgnoreCase(value)) {
                ip = value.split(",")[0].trim();
                break;
            }
        }

        String rawAgent = headers.getHeaderString("User-Agent");
        return new UserContextResponse(ip != null ? ip: "127.0.0.1", rawAgent != null ? rawAgent : "Unknown-Agent");
    }
}
