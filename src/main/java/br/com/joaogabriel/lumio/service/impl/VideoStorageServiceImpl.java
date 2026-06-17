package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.exception.ResourceNotFoundException;
import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.model.dto.response.TrailerUploadResponse;
import br.com.joaogabriel.lumio.model.dto.response.VideoUploadResponse;
import br.com.joaogabriel.lumio.model.entity.Course;
import br.com.joaogabriel.lumio.model.entity.Lesson;
import br.com.joaogabriel.lumio.model.entity.LessonVideo;
import br.com.joaogabriel.lumio.model.enumerations.MediaStatus;
import br.com.joaogabriel.lumio.repository.CourseRepository;
import br.com.joaogabriel.lumio.repository.LessonRepository;
import br.com.joaogabriel.lumio.repository.LessonVideoRepository;
import br.com.joaogabriel.lumio.service.S3StorageService;
import br.com.joaogabriel.lumio.service.VideoStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
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

    private static final long MAXIMUM_FILE_SIZE_BYTES = 2048L * 1024 * 1024;
    private static final Set<String> ALLOWED_VIDEO__TYPES = Set.of(
            "video/mp4",
            "video/quicktime",
            "video/x-matroska");

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

    @Override
    @Transactional
    public VideoUploadResponse initiateVideoUpload(UUID courseId, UUID lessonId, FileUploadRequest fileUploadRequest) {
        validateVideoType(fileUploadRequest.file().contentType());
        validateFileSize(fileUploadRequest.file().size());

        this.courseRepository.findByIdOptional(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course with id: " + courseId + " not found."));

        Lesson lesson = this.lessonRepository.findByIdOptional(lessonId)
                        .filter(l -> l.getCourse().getId().equals(courseId))
                                .orElseThrow(() -> new ResourceNotFoundException("Lesson with id: " + lessonId + " not found."));

        String keycloakId = this.jwt.getSubject();

        if (Boolean.FALSE.equals(this.courseRepository.isOwner(courseId, keycloakId))) {
            throw new ForbiddenException("Forbidden");
        }

        String key = String.format("%s/courses/%s/lessons/%s/%s",
                keycloakId, courseId, lessonId, fileUploadRequest.file().fileName());

        LOG.info("Initiating presigned URL for course video. Target Key: {}", key);

        LessonVideo lessonVideo = new LessonVideo(key, fileUploadRequest.file().fileName(), fileUploadRequest.file().size(),
                fileUploadRequest.file().contentType(), lesson, LocalDateTime.now(), MediaStatus.PENDING_UPLOAD);

        this.lessonVideoRepository.persist(lessonVideo);

        URL url = this.s3StorageService.generatePresignedUploadUrl(this.bucket,
                key, Duration.ofMinutes(15));
        LOG.info("Presigned URL for course video generated successfully.");
        return new VideoUploadResponse(
                url.toString(),
                key,
                LocalDateTime.now().plus(Duration.ofMinutes(15)));
    }

    private void validateVideoType(String contentType) {
        if (contentType == null || !ALLOWED_VIDEO__TYPES.contains(contentType)) {
            throw new BadRequestException("Invalid video content type.");
        }
    }

    private void validateFileSize(long size) {
        if (size > MAXIMUM_FILE_SIZE_BYTES) {
            throw new BadRequestException("File size is too large");
        }
    }

    @Override
    @Transactional
    public FileUploadResponse upload(UUID courseId, UUID lessonId, FileUploadRequest request) {
        Optional.ofNullable(this.courseRepository.findById(courseId))
                .orElseThrow(() -> new ResourceNotFoundException("Course with id: " + courseId + " not found into database."));

        Lesson lesson = this.lessonRepository.findByIdOptional(lessonId)
                .filter(l -> l.getCourse().getId().equals(courseId))
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found or does not belong to this course."));

        String keycloakId = jwt.getSubject();
        if (Boolean.FALSE.equals(this.courseRepository.isOwner(courseId, keycloakId))) {
            throw new ForbiddenException("Forbidden");
        }

        String key = String.format("%s/courses/%s/lessons/%s", keycloakId, courseId, lessonId);
        FileUploadResponse uploaded = this.s3StorageService.upload(request, this.bucket, key);
        LOG.info("File uploaded successfully.");
        LessonVideo lessonVideo = new LessonVideo(uploaded.key(), uploaded.filename(), uploaded.sizeInBytes(),
                uploaded.contentType(), lesson, uploaded.uploadAt(), MediaStatus.AVAILABLE);
        this.lessonVideoRepository.persist(lessonVideo);
        return uploaded;
    }




}
