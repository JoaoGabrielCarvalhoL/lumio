package br.com.joaogabriel.lumio.repository;

import br.com.joaogabriel.lumio.model.entity.CourseAnalytics;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CourseAnalyticsRepository implements PanacheRepositoryBase<CourseAnalytics, UUID> {

    @Transactional
    public void incrementStudentCount(UUID courseId) {
        update("totalStudents = totalStudents + 1, updatedAt = ?2 where id = ?1",
                courseId, LocalDateTime.now());
    }

    @Transactional
    public void updateAverageRating(UUID courseId, BigDecimal newAverage, Integer totalReviews) {
        update("averageRating = ?2, totalReviews = ?3, updatedAt = ?4 where id = ?1",
                courseId, newAverage, totalReviews, LocalDateTime.now());
    }

    public List<CourseAnalytics> findTopPerforming(int limit) {
        return find("order by totalStudents desc, averageRating desc")
                .range(0, limit - 1)
                .list();
    }
}
