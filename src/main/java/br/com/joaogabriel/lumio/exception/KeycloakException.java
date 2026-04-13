package br.com.joaogabriel.lumio.exception;

public class KeycloakException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public KeycloakException(String message) {
		super(message);
	}
	
	public KeycloakException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
