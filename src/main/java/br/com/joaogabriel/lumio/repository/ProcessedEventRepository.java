package br.com.joaogabriel.lumio.repository;

import br.com.joaogabriel.lumio.model.entity.ProcessedEvent;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.Optional;
import java.util.UUID;

public class ProcessedEventRepository implements PanacheRepositoryBase<ProcessedEvent, UUID> {

    public Optional<ProcessedEvent> findByEventId(String eventId) {
        return find("eventId", eventId).firstResultOptional();
    };
}
