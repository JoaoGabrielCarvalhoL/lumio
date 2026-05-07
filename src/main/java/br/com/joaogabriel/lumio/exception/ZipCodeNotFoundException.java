package br.com.joaogabriel.lumio.exception;

public class ZipCodeNotFoundException extends RuntimeException {
    public ZipCodeNotFoundException(final String message) {
        super(message);
    }

    public ZipCodeNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

//TODO: must be implemented handler
