package br.com.joaogabriel.lumio.model.dto.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AwsS3EventResponse(
        @JsonProperty("Records")
        List<AwsS3RecordResponse> records) {
}
