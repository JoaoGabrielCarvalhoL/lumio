package br.com.joaogabriel.lumio.mapper;

import br.com.joaogabriel.lumio.model.dto.response.CourseAnalyticsResponse;
import br.com.joaogabriel.lumio.model.entity.CourseAnalytics;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseAnalyticsMapper {

    CourseAnalyticsResponse toCourseAnalyticsResponse(final CourseAnalytics courseAnalytics);
}
