package br.com.joaogabriel.lumio.filter;

import br.com.joaogabriel.lumio.annotation.CheckResourceOwner;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.io.IOException;

@CheckResourceOwner
@Provider
public class ResourceOwnerFilter implements ContainerRequestFilter {
    private final JsonWebToken jwt;

    public ResourceOwnerFilter(JsonWebToken jwt) {
        this.jwt = jwt;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String resourceId = requestContext.getUriInfo().getPathParameters().getFirst("id");
        String subject = this.jwt.getSubject();
        boolean isAdmin = this.jwt.getGroups().contains("ADMIN");

        if (subject == null || (!subject.equals(resourceId) && !isAdmin)) {
            throw new ForbiddenException(String.format("Access denied"));
        }
    }
}
