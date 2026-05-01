package br.com.joaogabriel.lumio.exception;

public class SerializationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SerializationException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
