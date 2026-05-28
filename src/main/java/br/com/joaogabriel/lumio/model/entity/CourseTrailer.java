package br.com.joaogabriel.lumio.model.entity;

import br.com.joaogabriel.lumio.model.enumerations.MediaStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tb_course_trailer")
public class CourseTrailer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String s3Key;

    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaStatus status;

    public CourseTrailer() {}

    public CourseTrailer(String s3Key, Long fileSize, MediaStatus status) {
        this.s3Key = s3Key;
        this.fileSize = fileSize;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }

    public void activate(final MediaStatus mediaStatus, final Long fileSize) {
        this.setStatus(mediaStatus);
        this.setFileSize(fileSize);
    }
}
