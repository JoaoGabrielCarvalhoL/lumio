package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.exception.ResourceNotFoundException;
import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.model.entity.Lesson;
import br.com.joaogabriel.lumio.model.entity.LessonVideo;
import br.com.joaogabriel.lumio.repository.CourseRepository;
import br.com.joaogabriel.lumio.repository.LessonRepository;
import br.com.joaogabriel.lumio.repository.LessonVideoRepository;
import br.com.joaogabriel.lumio.service.S3StorageService;
import br.com.joaogabriel.lumio.service.VideoStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class VideoStorageServiceImpl implements VideoStorageService {
    private static final Logger LOG = LoggerFactory.getLogger(VideoStorageServiceImpl.class);

    private final S3StorageService s3StorageService;
    private final String bucket;
    private final JsonWebToken jwt;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonVideoRepository lessonVideoRepository;

    public VideoStorageServiceImpl(S3StorageService s3StorageService,
                                   @ConfigProperty(name = "S3_BUCKET_VIDEOS") String bucket,
                                   JsonWebToken jwt,
                                   CourseRepository courseRepository,
                                   LessonRepository lessonRepository,
                                   LessonVideoRepository lessonVideoRepository) {
        this.s3StorageService = s3StorageService;
        this.bucket = bucket;
        this.jwt = jwt;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.lessonVideoRepository = lessonVideoRepository;
    }

    @Transactional
    public FileUploadResponse upload(UUID courseId, UUID lessonId, FileUploadRequest request) {
        Optional.ofNullable(this.courseRepository.findById(courseId))
                .orElseThrow(() -> new ResourceNotFoundException("Course with id: " + courseId + " not found into database."));

        Lesson lesson = this.lessonRepository.findByIdOptional(lessonId)
                .filter(l -> l.getCourse().getId().equals(courseId))
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found or does not belong to this course."));

        String keycloakId = jwt.getSubject();
        if (!this.courseRepository.isOwner(courseId, keycloakId)) {
            throw new ForbiddenException("Forbidden");
        }

        String key = String.format("%s/courses/%s/lessons/%s", keycloakId, courseId, lessonId);
        FileUploadResponse uploaded = this.s3StorageService.upload(request, this.bucket, key);
        LOG.info("File uploaded successfully.");
        LessonVideo lessonVideo = new LessonVideo(uploaded.key(), uploaded.filename(), uploaded.sizeInBytes(),
                uploaded.contentType(), lesson, uploaded.uploadAt());
        this.lessonVideoRepository.persist(lessonVideo);
        return uploaded;
    }


}
