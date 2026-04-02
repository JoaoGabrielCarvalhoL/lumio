package br.com.joaogabriel.lumio.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistsException(String message) {
		super(message);
	}
	
	public ResourceAlreadyExistsException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
