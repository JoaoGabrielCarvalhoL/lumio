package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.exception.ResourceNotFoundException;
import br.com.joaogabriel.lumio.model.dto.response.UserCreatedEventResponse;
import br.com.joaogabriel.lumio.model.entity.User;
import br.com.joaogabriel.lumio.repository.UserRepository;
import br.com.joaogabriel.lumio.service.EmailService;
import io.quarkus.mailer.MailTemplate;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class EmailServiceImpl implements EmailService {

    private final static Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final UserRepository userRepository;
    private final MailTemplate welcome;

    public EmailServiceImpl(MailTemplate welcome, UserRepository userRepository) {
        this.welcome = welcome;
        this.userRepository = userRepository;
    }

    @Override
    public void sendWelcomeEmail(UUID id) {
        User user = Optional.ofNullable(userRepository.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        welcome.to(user.getEmail())
                .subject("Welcome to Lumio!")
                .data("name", user.getFirstName())
                .data("username", user.getUsername())
                .send()
                .await().atMost(java.time.Duration.ofSeconds(10));
        LOG.info("Email successfully sent to user {}.", user.getEmail());
    }
}
