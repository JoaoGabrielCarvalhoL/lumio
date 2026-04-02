package br.com.joaogabriel.lumio.exception;

public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
		super(message);
	}
	
	public BusinessException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
