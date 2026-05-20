package br.com.joaogabriel.lumio.model.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tb_course_analytics")
public class CourseAnalytics {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn
    private Course course;

    @Column(precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    private Integer enrolled;

    private Double completionRate = 0.0;


}
