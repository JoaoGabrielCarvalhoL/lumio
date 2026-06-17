package br.com.joaogabriel.lumio.model.dto.response;

import java.time.LocalDateTime;

public record VideoUploadResponse(
    String uploadUrl,
    String trailerKey,
    LocalDateTime expiresAt)
{ }
