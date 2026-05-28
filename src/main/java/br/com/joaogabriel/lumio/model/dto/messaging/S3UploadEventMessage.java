package br.com.joaogabriel.lumio.model.dto.messaging;

import br.com.joaogabriel.lumio.model.enumerations.S3EventType;

public record S3UploadEventMessage(
        String bucket,
        String key,
        Long size,
        S3EventType eventType
) {
}
