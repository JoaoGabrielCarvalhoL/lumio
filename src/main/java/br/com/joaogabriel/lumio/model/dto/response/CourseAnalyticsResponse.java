package br.com.joaogabriel.lumio.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CourseAnalyticsResponse(
        UUID id,
        BigDecimal averageRating,
        Integer enrolled,
        Double completionRate
) {
}
