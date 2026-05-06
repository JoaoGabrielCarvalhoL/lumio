package br.com.joaogabriel.lumio.service;

import java.util.UUID;

public interface EmailService {

    void sendWelcomeEmail(final UUID id);
}
