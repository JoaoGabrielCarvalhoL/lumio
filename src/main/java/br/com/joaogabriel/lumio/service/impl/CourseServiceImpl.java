package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.exception.ResourceNotFoundException;
import br.com.joaogabriel.lumio.mapper.CourseMapper;
import br.com.joaogabriel.lumio.model.dto.request.CourseCreateRequest;
import br.com.joaogabriel.lumio.model.dto.request.CourseUpdateRequest;
import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.CourseResponse;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.model.dto.response.TrailerUploadResponse;
import br.com.joaogabriel.lumio.model.entity.Category;
import br.com.joaogabriel.lumio.model.entity.Course;
import br.com.joaogabriel.lumio.model.entity.CourseTrailer;
import br.com.joaogabriel.lumio.model.enumerations.MediaStatus;
import br.com.joaogabriel.lumio.repository.CategoryRepository;
import br.com.joaogabriel.lumio.repository.CourseRepository;
import br.com.joaogabriel.lumio.service.CourseService;
import br.com.joaogabriel.lumio.service.ThumbnailStorageService;
import br.com.joaogabriel.lumio.service.TrailerStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ForbiddenException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CourseServiceImpl implements CourseService {

    private static final Logger LOG = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;
    private final ThumbnailStorageService thumbnailStorageService;
    private final TrailerStorageService trailerStorageService;
    private final JsonWebToken jwt;

    public CourseServiceImpl(CourseRepository courseRepository,
                             CategoryRepository categoryRepository,
                             CourseMapper courseMapper,
                             ThumbnailStorageService thumbnailStorageService,
                             TrailerStorageService trailerStorageService,
                             JsonWebToken jwt) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.courseMapper = courseMapper;
        this.thumbnailStorageService = thumbnailStorageService;
        this.trailerStorageService = trailerStorageService;
        this.jwt = jwt;
    }

    @Override
    public CourseResponse findById(UUID id) {
        LOG.info("Getting course by id {}", id);
        return Optional.ofNullable(this.courseRepository.findById(id))
                .map(this.courseMapper::toCourseResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    @Override
    public List<CourseResponse> findByName(String name) {
        LOG.info("Getting course by name {}", name);
        return this.courseRepository.findByNameContaining(name)
                .stream()
                .map(this.courseMapper::toCourseResponse).toList();
    }

    @Override
    public List<CourseResponse> findAllPublished(int page, int size) {
        LOG.info("Getting all published courses by page {} and size {}", page, size);
        return this.courseRepository.findAllPublished(page, size)
                .stream().map(courseMapper::toCourseResponse).toList();
    }

    @Override
    @Transactional
    public CourseResponse save(CourseCreateRequest courseCreateRequest, UUID categoryId) {
        LOG.info("Saving course {}", courseCreateRequest);

        Category category = Optional.ofNullable(this.categoryRepository.findById(categoryId))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
        Course course = this.courseMapper.toCourse(courseCreateRequest);
        course.setCategory(category);
        this.courseRepository.persist(course);
        return  courseMapper.toCourseResponse(course);
    }

    @Override
    @Transactional
    public CourseResponse update(CourseUpdateRequest courseUpdateRequest) {
        return null;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Course course = this.courseRepository.findByIdOptional(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        this.courseRepository.delete(course);
    }

    @Override
    @Transactional
    public void uploadThumbnail(UUID courseId, FileUploadRequest request) {
        Course course = this.courseRepository.findByIdOptional(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        String keycloakId = this.jwt.getSubject();

        if (Boolean.FALSE.equals(this.courseRepository.isOwner(courseId, keycloakId))) {
            throw new ForbiddenException("Forbidden");
        }

        String key = String.format("%s/courses/%s/thumbnails/", keycloakId, courseId);
        FileUploadResponse uploaded = this.thumbnailStorageService.upload(key, request);
        course.setThumbnail(uploaded.key());
        this.courseRepository.persist(course);
        LOG.info("Thumbnail uploaded successfully for courseId: {}.", courseId);
    }

    @Override
    @Transactional
    public TrailerUploadResponse uploadFreeTrailer(UUID courseId, FileUploadRequest request) {
        Course course = this.courseRepository.findByIdOptional(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        String keycloakId = this.jwt.getSubject();

        if (Boolean.FALSE.equals(this.courseRepository.isOwner(courseId, keycloakId))) {
            throw new ForbiddenException("Forbidden");
        }
        String key = String.format("%s/courses/%s/trailers/%s", keycloakId, courseId,
                request.file().fileName());
        LOG.info("Initiating asynchronous trailer upload flow for courseId: {}.", courseId);

        course.setTrailer(new CourseTrailer(key, request.file().size(), MediaStatus.PENDING_UPLOAD));
        this.courseRepository.persist(course);
        return this.trailerStorageService.initiateUpload(key, request);
    }

    @Override
    public void processTrailerActivation(String key, Long size) {
        LOG.info("Activating trailer for key: {}", key);
        Course course = this.courseRepository.findByTrailerKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found for key: " + key));

        CourseTrailer courseTrailer = course.getTrailer();
        courseTrailer.activate(MediaStatus.AVAILABLE, size);
        this.courseRepository.persist(course);
        LOG.info("Trailer for course ID {} successfully marked as AVAILABLE.", course.getId());

    }
}
