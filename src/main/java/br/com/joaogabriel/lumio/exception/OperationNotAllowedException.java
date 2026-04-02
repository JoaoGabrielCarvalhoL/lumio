package br.com.joaogabriel.lumio.exception;

public class OperationNotAllowedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OperationNotAllowedException(String message) {
		super(message);
	}
	
	public OperationNotAllowedException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	
}
