package br.com.joaogabriel.lumio.resource;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;

@Path("/users")
public class UserResource {
	
	@Context
	private HttpHeaders headers;
	
	

}
