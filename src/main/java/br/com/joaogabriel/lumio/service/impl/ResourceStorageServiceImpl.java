package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.service.ResourceStorageService;
import br.com.joaogabriel.lumio.service.S3StorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@ApplicationScoped
public class ResourceStorageServiceImpl implements ResourceStorageService {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceStorageServiceImpl.class);
    private final String bucket;
    private final S3StorageService s3StorageService;
    private static final long MAXIMUM_FILE_SIZE_BYTES = 25L * 1024 * 1024;
    private static final Set<String> ALLOWED_RESOURCE_TYPES = Set.of("application/pdf", "application/zip",
            "application/x-zip-compressed", "application/x-rar-compressed", "application/x-compressed", "image/jpeg",
            "image/png", "text/plain", "text/csv", "application/json", "application/xml",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation");

    public ResourceStorageServiceImpl(@ConfigProperty(name = "S3_BUCKET_RESOURCES") String bucket,
                                      S3StorageService s3StorageService) {
        this.bucket = bucket;
        this.s3StorageService = s3StorageService;
    }

    @Override
    public FileUploadResponse upload(String key, FileUploadRequest request) {
        validateFileSize(request.file().size());
        validateResourceType(request.file().contentType());
        LOG.info("Uploading file {} to bucket {}", key, bucket);
        return this.s3StorageService.upload(request, bucket, key);
    }

    private void validateResourceType(String resourceType) {
        if (resourceType == null || !ALLOWED_RESOURCE_TYPES.contains(resourceType.trim().toLowerCase())) {
            throw new BadRequestException("Invalid resource type: " + resourceType);
        }
    }

    private void validateFileSize(long size) {
        if (size > MAXIMUM_FILE_SIZE_BYTES) {
            throw new BadRequestException("Resource exceeds the maximum allowed size of 25MB. File size: " + size);
        }
    }
}
