package br.com.joaogabriel.lumio.domain.service.impl;

import br.com.joaogabriel.lumio.client.repository.UserRepository;
import br.com.joaogabriel.lumio.domain.service.UserUniquenessChecker;

public class UserUniquenessCheckerImpl implements UserUniquenessChecker {

	private final UserRepository userRepository;
	
	public UserUniquenessCheckerImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Boolean existsByUsername(String username) {
		return this.userRepository.existsByUsername(username);
	}

	@Override
	public Boolean existsByEmail(String email) {
		return this.userRepository.existsByEmail(email);
	}

}
