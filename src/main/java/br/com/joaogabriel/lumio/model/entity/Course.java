package br.com.joaogabriel.lumio.model.entity;

import br.com.joaogabriel.lumio.model.enumerations.CourseLevel;
import br.com.joaogabriel.lumio.model.enumerations.CourseStatus;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_courses", indexes = {
        @Index(name = "idx_course_name", columnList = "name")
})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String thumbnail;

    @Column(nullable = false)
    private String trailer;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Boolean isPublished;

    private Integer totalDuration;

    private Integer totalLessons;

    @Column(nullable = false)
    private String language;

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @Enumerated(EnumType.STRING)
    private CourseStatus status;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Category category;

    private CourseAnalytics analytics;

    public Course() {}

    public UUID getId() {
        return id;
    }
}
