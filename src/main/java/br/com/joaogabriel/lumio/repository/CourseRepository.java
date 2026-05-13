package br.com.joaogabriel.lumio.repository;

import br.com.joaogabriel.lumio.model.entity.Course;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class CourseRepository implements PanacheRepositoryBase<Course, UUID> {

    public Boolean isOwner(UUID courseId, String keycloakUserId) {
        return count("id = ?1 and user.keycloakId = ?2", courseId, keycloakUserId) > 0;
    }

}
