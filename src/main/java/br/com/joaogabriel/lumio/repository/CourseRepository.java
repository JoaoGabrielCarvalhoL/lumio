package br.com.joaogabriel.lumio.repository;

import br.com.joaogabriel.lumio.model.dto.response.CourseResponse;
import br.com.joaogabriel.lumio.model.entity.Course;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CourseRepository implements PanacheRepositoryBase<Course, UUID> {

    public Boolean isOwner(UUID courseId, String keycloakUserId) {
        return count("id = ?1 and user.keycloakId = ?2", courseId, keycloakUserId) > 0;
    }

    public Course findByName(String name) {
        return find("name = ?1", name).firstResult();
    }

    public List<Course> findByNameContaining(String name) {
        return find("name like ?1", name).list();
    }

    public List<Course> findAllPublished(int page, int size) {
        return find("isPublished = ?1", true)
                .page(Page.of(page, size))
                .list();
    }

}
