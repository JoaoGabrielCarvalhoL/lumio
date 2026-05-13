package br.com.joaogabriel.lumio.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_lesson_materials")
public class LessonMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private String s3Key;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long sizeInBytes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;
}
