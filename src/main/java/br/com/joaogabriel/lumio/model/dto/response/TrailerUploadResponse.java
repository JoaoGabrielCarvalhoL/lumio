package br.com.joaogabriel.lumio.model.dto.response;

import java.time.LocalDateTime;

public record TrailerUploadResponse(
    String uploadUrl,
    String trailerKey,
    LocalDateTime expiresAt)
{ }
