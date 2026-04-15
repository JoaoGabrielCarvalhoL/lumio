package br.com.joaogabriel.lumio.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.joaogabriel.lumio.validate.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {

	String message() default "Username already in use.";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
