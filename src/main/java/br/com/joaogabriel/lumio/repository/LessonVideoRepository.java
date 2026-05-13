package br.com.joaogabriel.lumio.repository;

import br.com.joaogabriel.lumio.model.entity.LessonVideo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class LessonVideoRepository implements PanacheRepositoryBase<LessonVideo, UUID> {
}
