package br.com.joaogabriel.lumio.model.dto.request;

public record TrailerUploadInitiateRequest(
        String filename,
        String contentType,
        Long sizeInBytes
) {
}
