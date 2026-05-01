package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.response.UserCreatedEventResponse;

public interface EmailService {

    void sendWelcomeEmail(UserCreatedEventResponse user);
}
