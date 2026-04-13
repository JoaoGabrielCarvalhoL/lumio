package br.com.joaogabriel.lumio.exception.mapper;

import br.com.joaogabriel.lumio.exception.KeycloakOperationException;

public class KeycloakErrorMapper {
	
	private KeycloakErrorMapper() {}
	
	public static KeycloakOperationException fromStatus(int status, String message, Throwable cause) {
		return new KeycloakOperationException(KeycloakErrorContext.from(status, message, cause));
    	}
	
	public record KeycloakErrorContext(
			int status, 
			boolean retryable, 
			String message, 
			Throwable cause) {
		
		public static KeycloakErrorContext from(int status, String message, Throwable cause) {
	        return new KeycloakErrorContext(status, isRetryable(status), message, cause);
	    }
		
		private static boolean isRetryable(int status) {
            return status >= 500;

        }

	}
}


