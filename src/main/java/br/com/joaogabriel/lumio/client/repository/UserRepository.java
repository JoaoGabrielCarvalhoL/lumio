package br.com.joaogabriel.lumio.client.repository;

import java.util.UUID;

import br.com.joaogabriel.lumio.model.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, UUID> {
	
	public Boolean existsByUsername(String username) {
		return count("username", username) > 0;
	}
	
	public Boolean existsByEmail(String email) {
		return count("email", email) > 0;
	}

}
