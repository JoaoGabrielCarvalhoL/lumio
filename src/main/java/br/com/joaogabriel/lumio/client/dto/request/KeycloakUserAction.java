package br.com.joaogabriel.lumio.client.dto.request;

import com.fasterxml.jackson.annotation.JsonValue;

public enum KeycloakUserAction {
	UPDATE_PASSWORD("UPDATE_PASSWORD"),
    VERIFY_EMAIL("VERIFY_EMAIL"),
    CONFIGURE_TOTP("CONFIGURE_TOTP"),
    UPDATE_PROFILE("UPDATE_PROFILE"),
    RESET_PASSWORD("RESET_PASSWORD");

    private final String value;

    KeycloakUserAction(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
