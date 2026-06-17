package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.exception.ResourceNotFoundException;
import br.com.joaogabriel.lumio.mapper.LessonMapper;
import br.com.joaogabriel.lumio.model.dto.request.LessonCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.LessonResponse;
import br.com.joaogabriel.lumio.model.entity.Course;
import br.com.joaogabriel.lumio.model.entity.Lesson;
import br.com.joaogabriel.lumio.model.enumerations.MediaStatus;
import br.com.joaogabriel.lumio.repository.CourseRepository;
import br.com.joaogabriel.lumio.repository.LessonRepository;
import br.com.joaogabriel.lumio.repository.LessonVideoRepository;
import br.com.joaogabriel.lumio.service.LessonVideoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@ApplicationScoped
public class LessonVideoServiceImpl implements LessonVideoService {

    private final LessonRepository lessonRepository;
    private final LessonVideoRepository lessonVideoRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper  lessonMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonVideoServiceImpl.class);

    public LessonVideoServiceImpl(LessonRepository lessonRepository,
                                  LessonVideoRepository lessonVideoRepository,
                                  CourseRepository courseRepository,
                                  LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonVideoRepository = lessonVideoRepository;
        this.courseRepository = courseRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    @Transactional
    public void processVideoActivation(String key, Long size) {
        LOGGER.info("Activating video for key: {}", key);
        this.lessonVideoRepository.findByVideoKey(key).ifPresentOrElse(lessonVideo -> {

            lessonVideo.setSizeInBytes(size);
            lessonVideo.setStatus(MediaStatus.AVAILABLE);

            this.lessonVideoRepository.persist(lessonVideo);
            LOGGER.info("Video with key: {} successfully activated.", key);

        }, () -> {
            LOGGER.error("CRITICAL: Received activation event for key '{}' but no metadata matching was found!", key);
            throw new ResourceNotFoundException("Video metadata not found for key: " + key);
        });
    }

    @Override
    public LessonResponse save(UUID courseId, LessonCreateRequest request) {
        Course course = this.courseRepository.findByIdOptional(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        Lesson lesson = this.lessonMapper.toLesson(request);
        lesson.setCourse(course);
        this.lessonRepository.persist(lesson);
        LOGGER.info("Lesson saved successfully.");
        return lessonMapper.toLessonResponse(lesson);
    }
}
