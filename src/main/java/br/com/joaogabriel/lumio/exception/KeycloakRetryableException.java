package br.com.joaogabriel.lumio.exception;

import br.com.joaogabriel.lumio.exception.mapper.KeycloakErrorMapper;

public class KeycloakRetryableException extends KeycloakOperationException {
    public KeycloakRetryableException(KeycloakErrorMapper.KeycloakErrorContext context) {
        super(context);
    }
}
