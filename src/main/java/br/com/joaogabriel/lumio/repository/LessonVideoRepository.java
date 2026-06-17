package br.com.joaogabriel.lumio.repository;

import br.com.joaogabriel.lumio.model.entity.LessonVideo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class LessonVideoRepository implements PanacheRepositoryBase<LessonVideo, UUID> {

    public Optional<LessonVideo> findByVideoKey(String key) {
        return find("s3Key = ?1", key).firstResultOptional();
    }
}
