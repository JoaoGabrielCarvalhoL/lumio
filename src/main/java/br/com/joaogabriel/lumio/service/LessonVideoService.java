package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.LessonCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.LessonResponse;

import java.util.UUID;

public interface LessonVideoService {

    void processVideoActivation(final String key, final Long size);
    LessonResponse save(final UUID courseId, final LessonCreateRequest request);
}
