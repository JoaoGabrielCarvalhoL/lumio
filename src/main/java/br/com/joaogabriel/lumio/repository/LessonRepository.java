package br.com.joaogabriel.lumio.repository;

import br.com.joaogabriel.lumio.model.entity.Lesson;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class LessonRepository implements PanacheRepositoryBase<Lesson, UUID> {
}
