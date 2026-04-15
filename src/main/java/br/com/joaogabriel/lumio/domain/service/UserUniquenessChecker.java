package br.com.joaogabriel.lumio.domain.service;

public interface UserUniquenessChecker {
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);

}
