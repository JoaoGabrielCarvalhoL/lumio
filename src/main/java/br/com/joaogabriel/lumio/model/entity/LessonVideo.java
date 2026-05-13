package br.com.joaogabriel.lumio.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_lesson_videos")
public class LessonVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, updatable = false)
    private String s3Key;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private Long sizeInBytes;

    @Column(nullable = false)
    private String contentType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Lesson lesson;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    public LessonVideo() {}

    public LessonVideo(String s3Key, String originalFilename, Long sizeInBytes, String contentType, Lesson lesson,
                       LocalDateTime uploadedAt) {
        this.s3Key = s3Key;
        this.originalFilename = originalFilename;
        this.sizeInBytes = sizeInBytes;
        this.contentType = contentType;
        this.lesson = lesson;
        this.uploadedAt = uploadedAt;
    }


}
