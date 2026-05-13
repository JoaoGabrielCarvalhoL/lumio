package br.com.joaogabriel.lumio.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import br.com.joaogabriel.lumio.model.dto.response.UserContextResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.joaogabriel.lumio.event.producer.KeycloakUserCreateQueueProducer;
import br.com.joaogabriel.lumio.model.dto.request.UserCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.ProvisioningResponse;
import br.com.joaogabriel.lumio.model.entity.UserProvisioning;
import br.com.joaogabriel.lumio.model.enumerations.ProvisioningStatus;
import br.com.joaogabriel.lumio.repository.UserProvisioningRepository;
import br.com.joaogabriel.lumio.util.Serializer;
import br.com.joaogabriel.lumio.service.UserService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private final KeycloakUserCreateQueueProducer producer;
	private final UserProvisioningRepository userProvisioningRepository;
	private final Serializer serializer;

	public UserServiceImpl(KeycloakUserCreateQueueProducer producer,
			UserProvisioningRepository userProvisioningRepository, Serializer serializer) {
		this.producer = producer;
		this.userProvisioningRepository = userProvisioningRepository;
		this.serializer = serializer;
	}

	@Override
	@Transactional
	public ProvisioningResponse save(UserCreateRequest userCreateRequest, UserContextResponse userContext) {
		logger.info("Starting provisioning process for user: {}", userCreateRequest.username());
		String externalId = generateExternalId(userCreateRequest);
		String payload = this.serializer.serialize(userCreateRequest);

		UserProvisioning provisioning = new UserProvisioning(userCreateRequest.username(), userCreateRequest.email(), externalId, 0, null,
				ProvisioningStatus.PENDING_PROVISIONING, payload, userContext.ip(), userContext.rawAgent());

		this.userProvisioningRepository.persist(provisioning);
		producer.send(provisioning.getId());

		logger.info("User {} sent to provisioning queue with ID: {}",
				userCreateRequest.username(), provisioning.getId());

		return new ProvisioningResponse(provisioning.getId(), ProvisioningStatus.PENDING_PROVISIONING);
	}

	private String generateExternalId(UserCreateRequest request) {
		byte[] seed = String.format("%s:%s", request.username(), request.email())
				.getBytes(StandardCharsets.UTF_8);
		return UUID.nameUUIDFromBytes(seed).toString();
	}
	

	
}
