package br.com.joaogabriel.lumio.processor;

import br.com.joaogabriel.lumio.client.dto.request.KeycloakCreateUserRequest;
import br.com.joaogabriel.lumio.client.dto.request.KeycloakCredentialRequest;
import br.com.joaogabriel.lumio.client.dto.request.KeycloakUserAction;
import br.com.joaogabriel.lumio.event.producer.UserCreatedEventProducer;
import br.com.joaogabriel.lumio.exception.AlreadyProcessedException;
import br.com.joaogabriel.lumio.exception.ResourceNotFoundException;
import br.com.joaogabriel.lumio.model.UserProvisioningResult;
import br.com.joaogabriel.lumio.model.dto.request.UserCreateRequest;
import br.com.joaogabriel.lumio.model.entity.User;
import br.com.joaogabriel.lumio.model.entity.UserProvisioning;
import br.com.joaogabriel.lumio.model.enumerations.ProvisioningStatus;
import br.com.joaogabriel.lumio.repository.UserProvisioningRepository;
import br.com.joaogabriel.lumio.repository.UserRepository;
import br.com.joaogabriel.lumio.service.KeycloakManagementService;
import br.com.joaogabriel.lumio.util.Serializer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserProvisioningProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(UserProvisioningProcessor.class);
    private static final int MAX_RETRIES = 3;

    private final UserProvisioningRepository userProvisioningRepository;
    private final UserRepository userRepository;
    private final KeycloakManagementService  keycloakManagementService;
    private final Serializer serializer;
    private final UserCreatedEventProducer userCreatedEventProducer;

    public UserProvisioningProcessor(UserProvisioningRepository userProvisioningRepository, UserRepository userRepository,
                                     KeycloakManagementService keycloakManagementService, Serializer serializer,
                                     UserCreatedEventProducer userCreatedEventProducer) {
        this.userProvisioningRepository = userProvisioningRepository;
        this.userRepository = userRepository;
        this.keycloakManagementService = keycloakManagementService;
        this.serializer = serializer;
        this.userCreatedEventProducer = userCreatedEventProducer;
    }

    @Transactional
    public void process(UUID provisioningId) {
        UserProvisioning provisioning = Optional.ofNullable(this.userProvisioningRepository.findById(provisioningId, LockModeType.PESSIMISTIC_WRITE))
                .orElseThrow(() -> new ResourceNotFoundException("Provisioning with id: " + provisioningId + " not found"));

        if (ProvisioningStatus.CREATED.equals(provisioning.getStatus())) {
            LOG.warn("User {} already provisioned. Skipping.", provisioning.getUsername());
            throw new AlreadyProcessedException("User " + provisioning.getUsername() + " already provisioned");
        }

        if (ProvisioningStatus.PROCESSING.equals(provisioning.getStatus())) {
            LOG.info("User {} is already being processed by another thread. Skipping.", provisioning.getUsername());
            throw new AlreadyProcessedException("User " + provisioning.getUsername() + " already provisioned");
        }

        provisioning.setStatus(ProvisioningStatus.PROCESSING);
        this.userProvisioningRepository.flush();

        try {
            UserCreateRequest request = this.serializer.deserialize(provisioning.getPayload(), UserCreateRequest.class);
            KeycloakCreateUserRequest keycloakCreateUserRequest = toKeycloakCreateUserRequest(request, provisioning.getExternalId());

            UserProvisioningResult provisioningResult = keycloakManagementService.createUser(keycloakCreateUserRequest);

            if (provisioningResult.status().equals(ProvisioningStatus.CREATED)) {
                User user = buildUserFromProvisioning(request, provisioning.getExternalId(), provisioningResult);
                this.userRepository.persist(user);

                provisioning.setStatus(ProvisioningStatus.CREATED);
                provisioning.setErrorMessage(null);

                this.userCreatedEventProducer.sendSuccessNotification(user.getId().toString());

                LOG.info("User {} successfully created in Keycloak and Local DB.", provisioning.getUsername());
            } else {
                handleRetry(provisioningId, "Keycloak returned non-success status: " + provisioningResult.status());
            }
        } catch (Exception exception) {
            LOG.error("Critical error processing provisioning ID: {}", provisioningId, exception);
            handleRetry(provisioningId, exception.getMessage());
            throw exception;
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void handleRetry(UUID provisioningId, String error) {
        UserProvisioning provisioning = Optional.ofNullable(this.userProvisioningRepository.findById(provisioningId))
                .orElseThrow(() -> new ResourceNotFoundException("Provisioning with id: " + provisioningId + " not found"));

        int currentRetry = provisioning.getRetryCount() + 1;
        provisioning.setRetryCount(currentRetry);
        provisioning.setErrorMessage(error);

        if (currentRetry >= MAX_RETRIES) {
            provisioning.setStatus(ProvisioningStatus.FAILED);
            LOG.error("Max retries reached for user {}. Marking as FAILED.", provisioning.getUsername());
        } else {
            provisioning.setStatus(ProvisioningStatus.PENDING_PROVISIONING);
            LOG.warn("Retry {}/{} for user {}.", currentRetry, MAX_RETRIES, provisioning.getUsername());
        }
    }

    private KeycloakCreateUserRequest toKeycloakCreateUserRequest(UserCreateRequest user, String externalId) {
        KeycloakCredentialRequest password = new KeycloakCredentialRequest("password", user.password(), false);
        return new KeycloakCreateUserRequest(user.username(), user.email(), true, false,
                user.firstName(), user.lastName(), List.of(KeycloakUserAction.VERIFY_EMAIL, KeycloakUserAction.CONFIGURE_TOTP),
                List.of(password), Map.of(
                "externalId", List.of(externalId),
                "externalSystem", List.of("lumio")));
    }

    private User buildUserFromProvisioning(UserCreateRequest user, String externalId,
                                           UserProvisioningResult result) {
        return new User(result.keycloakId(), externalId, user.username(), user.email(),
                user.firstName(), user.lastName());
    }

}
