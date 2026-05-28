package br.com.joaogabriel.lumio.messaging.consumer;

import br.com.joaogabriel.lumio.model.entity.ProcessedEvent;
import br.com.joaogabriel.lumio.repository.ProcessedEventRepository;
import br.com.joaogabriel.lumio.service.EmailService;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserNotificationConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(UserNotificationConsumer.class);

    private final EmailService emailService;
    private final ProcessedEventRepository processedEventRepository;

    public UserNotificationConsumer(EmailService emailService, ProcessedEventRepository processedEventRepository) {
        this.emailService = emailService;
        this.processedEventRepository = processedEventRepository;
    }

    @Incoming("user-notifiable-in")
    @Acknowledgment(Acknowledgment.Strategy.POST_PROCESSING)
    @RunOnVirtualThread
    @Transactional
    public void onUserCreated(String id) {
        String eventId = String.format("welcome-email-%s", id);
        Optional<ProcessedEvent> processed = this.processedEventRepository.findByEventId(eventId);
        if(processed.isEmpty()) {
            LOG.info("Received notification event for email: {}", id);
            try {
                emailService.sendWelcomeEmail(UUID.fromString(id));
                this.processedEventRepository.persist(new ProcessedEvent(eventId));
            } catch (Exception e) {
                LOG.error("Failed to send welcome email to {}", id, e);
                throw e;
            }
        } else {
            LOG.warn("Duplicate event detected. Email already processed for eventId: {}", eventId);
        }
    }

}
