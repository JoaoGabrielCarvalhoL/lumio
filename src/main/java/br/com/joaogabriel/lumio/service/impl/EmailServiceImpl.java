package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.model.dto.response.UserCreatedEventResponse;
import br.com.joaogabriel.lumio.service.EmailService;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MailTemplate;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class EmailServiceImpl implements EmailService {

    private final static Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final MailTemplate welcome;

    public EmailServiceImpl(MailTemplate welcome) {
        this.welcome = welcome;
    }

    @Override
    public void sendWelcomeEmail(UserCreatedEventResponse user) {
        welcome.to(user.email())
                .subject("Welcome to Lumio!")
                .data("name", user.firstName())
                .data("username", user.firstName())
                .send()
                .subscribe().with(
                        success -> LOG.info("Email successfully sent to user {}.", user.email()),
                        failure -> LOG.error("Failed to send email to user {}. Cause: {}", user.email(), failure)
                );
    }
}
