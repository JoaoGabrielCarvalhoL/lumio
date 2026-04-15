package br.com.joaogabriel.lumio.validate;

import java.util.Objects;

import br.com.joaogabriel.lumio.annotation.UniqueUsername;
import br.com.joaogabriel.lumio.domain.service.UserUniquenessChecker;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

	private final UserUniquenessChecker checker;
	
	public UniqueUsernameValidator(UserUniquenessChecker checker) {
		this.checker = checker;
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (Objects.isNull(value) || value.isBlank()) return true;
		return !this.checker.existsByUsername(value);
	}

}
