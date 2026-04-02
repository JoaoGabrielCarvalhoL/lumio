package br.com.joaogabriel.lumio.exception;

public class ExternalServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ExternalServiceException(String message) {
		super(message);
	}
	
	public ExternalServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	

}
