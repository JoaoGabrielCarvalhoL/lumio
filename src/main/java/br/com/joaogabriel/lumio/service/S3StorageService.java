package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;

import java.net.URL;
import java.time.Duration;

public interface S3StorageService {

    FileUploadResponse upload(final FileUploadRequest request, final String bucket, final String key);

    Boolean exists(final String bucket, final String key);

    URL generatePresignedUrl(final String bucket, final String key, Duration duration);

    void delete(final String bucket, final String key);
}
