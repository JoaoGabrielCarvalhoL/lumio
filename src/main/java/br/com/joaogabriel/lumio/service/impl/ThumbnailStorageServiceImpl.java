package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.service.S3StorageService;
import br.com.joaogabriel.lumio.service.ThumbnailStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@ApplicationScoped
public class ThumbnailStorageServiceImpl implements ThumbnailStorageService {
    private final String bucket;
    private final S3StorageService  s3StorageService;
    private static final Logger LOG = LoggerFactory.getLogger(ThumbnailStorageServiceImpl.class.getName());
    private static final long MAXIMUM_FILE_SIZE_BYTES = 5L * 1024 * 1024;
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/webp", "image/bmp", "image/gif");

    public ThumbnailStorageServiceImpl(@ConfigProperty(name = "S3_BUCKET_THUMBNAILS") String bucket,
                                       S3StorageService s3StorageService) {
        this.bucket = bucket;
        this.s3StorageService = s3StorageService;
    }

    @Override
    public FileUploadResponse upload(String key, FileUploadRequest request) {
        validateContentType(request.file().contentType());
        validateFileSize(request.file().size());
        return this.s3StorageService.upload(request, bucket, key);
    }

    private void validateContentType(String contentType) {
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.trim().toLowerCase())) {
            throw new BadRequestException(contentType + " is not a valid file type.");
        }
    }

    private void validateFileSize(long size) {
        if (size > MAXIMUM_FILE_SIZE_BYTES) {
            throw new BadRequestException("The file exceeds the maximum allowed size. Maximum allowed: " +
                    MAXIMUM_FILE_SIZE_BYTES);
        }
    }
}
