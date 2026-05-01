package br.com.joaogabriel.lumio.event.consumer;

import br.com.joaogabriel.lumio.model.dto.response.UserCreatedEventResponse;
import br.com.joaogabriel.lumio.service.EmailService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class UserNotificationConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(UserNotificationConsumer.class);

    private final EmailService emailService;

    public UserNotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @Incoming("user-notifiable-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    @RunOnVirtualThread
    public void onUserCreated(UserCreatedEventResponse event) {
        LOG.info("Received notification event for email: {}", event.email());
        try {
            emailService.sendWelcomeEmail(event);
        } catch (Exception e) {
            LOG.error("Failed to send welcome email to {}", event.email(), e);
            throw e;
        }
    }

}
