package br.com.joaogabriel.lumio.exception;

public class InvalidZipCodeException extends RuntimeException {
    public InvalidZipCodeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

//TODO: must be implemented handler