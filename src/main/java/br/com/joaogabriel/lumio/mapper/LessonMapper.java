package br.com.joaogabriel.lumio.mapper;

import br.com.joaogabriel.lumio.model.dto.request.LessonCreateRequest;
import br.com.joaogabriel.lumio.model.dto.request.LessonUpdateRequest;
import br.com.joaogabriel.lumio.model.dto.response.LessonResponse;
import br.com.joaogabriel.lumio.model.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LessonMapper {

    Lesson toLesson(final LessonCreateRequest request);

    Lesson toLesson(final LessonUpdateRequest request);

    LessonResponse toLessonResponse(final Lesson lesson);
}
