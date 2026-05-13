package br.com.joaogabriel.lumio.model.dto.response;

import java.time.LocalDateTime;

public record FileUploadResponse(
        String filename,
        String key,
        Long sizeInBytes,
        String contentType,
        LocalDateTime uploadAt
) {
}
