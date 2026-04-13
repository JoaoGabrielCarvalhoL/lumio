package br.com.joaogabriel.lumio.exception;

import br.com.joaogabriel.lumio.exception.mapper.KeycloakErrorMapper.KeycloakErrorContext;

public class KeycloakOperationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private final KeycloakErrorContext errorContext;
    
    public KeycloakOperationException(KeycloakErrorContext errorContext) {
    		super(errorContext.message());
    		this.errorContext = errorContext;
    		
    }

	public KeycloakErrorContext getErrorContext() {
		return errorContext;
	}

    

}
