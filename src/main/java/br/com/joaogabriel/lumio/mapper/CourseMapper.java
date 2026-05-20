package br.com.joaogabriel.lumio.mapper;

import br.com.joaogabriel.lumio.model.dto.request.CourseCreateRequest;
import br.com.joaogabriel.lumio.model.dto.request.CourseUpdateRequest;
import br.com.joaogabriel.lumio.model.dto.response.CourseResponse;
import br.com.joaogabriel.lumio.model.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    Course toCourse(final CourseCreateRequest courseCreateRequest);

    Course toCourse(final CourseUpdateRequest courseUpdateRequest);

    CourseResponse toCourseResponse(final Course course);
}
