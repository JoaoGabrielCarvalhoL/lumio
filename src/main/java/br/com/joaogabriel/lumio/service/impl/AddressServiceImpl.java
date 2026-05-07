package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.exception.ResourceNotFoundException;
import br.com.joaogabriel.lumio.model.dto.request.AddressCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.AddressResponse;
import br.com.joaogabriel.lumio.model.entity.User;
import br.com.joaogabriel.lumio.repository.UserRepository;
import br.com.joaogabriel.lumio.service.AddressService;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AddressServiceImpl implements AddressService {
    private final Logger LOG = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final UserRepository userRepository;

    public AddressServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AddressResponse save(AddressCreateRequest address, UUID userId) {
        User user = Optional.ofNullable(this.userRepository.findById(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found. Id: " + userId));
        //TODO: AddressMapper to convert dtoRequest into entity.
        //TODO: Persist address after set address.setUser();

        return null;

    }
}
