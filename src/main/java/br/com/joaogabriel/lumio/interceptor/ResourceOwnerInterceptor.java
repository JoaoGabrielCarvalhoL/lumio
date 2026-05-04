package br.com.joaogabriel.lumio.interceptor;

import br.com.joaogabriel.lumio.annotation.CheckResourceOwner;
import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.container.ResourceContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.UUID;

@CheckResourceOwner
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 10)
public class ResourceOwnerInterceptor {

    private final JsonWebToken jwt;
    private final ResourceContext resourceContext;

    public ResourceOwnerInterceptor(JsonWebToken jwt, ResourceContext resourceContext) {
        this.jwt = jwt;
        this.resourceContext = resourceContext;
    }

    @AroundInvoke
    public Object intercept(InvocationContext invocationContext) throws Throwable {
        Method method = invocationContext.getMethod();
        Object[] parameters = invocationContext.getParameters();
        Parameter[] methodParameters = method.getParameters();
        String resourceId = null;


        for (int i = 0; i < methodParameters.length; i++) {
            PathParam pathParam = methodParameters[i].getAnnotation(PathParam.class);
            if (pathParam != null && "id".equals(pathParam.value())) {
                resourceId = parameters[i].toString();
                break;
            }
        }

        String subject = this.jwt.getSubject();
        boolean isAdmin = jwt.getGroups().contains("ADMIN");

        if (subject == null || (!subject.equals(resourceId) && !isAdmin)) {
            throw new ForbiddenException("User is not allowed to access resource");
        }
        return invocationContext.proceed();
    }

}
