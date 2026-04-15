package br.com.joaogabriel.lumio.model.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_users", indexes = {
	@Index(name = "idx_user_email", columnList = "email"), 
	@Index(name = "idx_user_username", columnList = "username"),
    @Index(name = "idx_user_keycloak_id", columnList = "keycloak_id"),
    @Index(name = "idx_user_external_id", columnList = "external_id")
})
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(nullable = false, unique = true, updatable = false, name = "keycloak_id")
	private String keycloakId;
	
	@Column(nullable = false, unique = true, updatable = false, name = "external_id")
	private String externalId;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false, unique = true, updatable = false)
	private String email;
	
	@Column(nullable = false, length = 100)
	private String firstName;
	
	@Column(nullable = false, length = 200)
	private String lastName;
	
	@OneToMany(mappedBy = "user")
	private List<Address> addresses;
	
	public User() {}

}
