package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.model.dto.response.TrailerUploadResponse;
import br.com.joaogabriel.lumio.service.S3StorageService;
import br.com.joaogabriel.lumio.service.TrailerStorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@ApplicationScoped
public class TrailerStorageServiceImpl implements TrailerStorageService {
    private final String bucket;
    private final S3StorageService s3StorageService;

    private static final Logger LOG = LoggerFactory.getLogger(TrailerStorageServiceImpl.class);
    private static final long MAXIMUM_FILE_SIZE_BYTES = 100L * 1024 * 1024;
    private static final Set<String> ALLOWED_VIDEO__TYPES = Set.of(
            "video/mp4",
            "video/quicktime",
            "video/x-matroska");

    public TrailerStorageServiceImpl(@ConfigProperty(name = "S3_BUCKET_TRAILERS") String bucket,
                                     S3StorageService s3StorageService) {
        this.bucket = bucket;
        this.s3StorageService = s3StorageService;
    }

    @Override
    public FileUploadResponse upload(String key, FileUploadRequest request) {
        return this.s3StorageService.upload(request, bucket, key);
    }

    @Override
    public TrailerUploadResponse initiateUpload(String key, FileUploadRequest request) {
        validateVideoType(request.file().contentType());
        validateFileSize(request.file().size());

        LOG.info("Initiating presigned URL for course trailer. Target Key: {}", key);

        URL url = this.s3StorageService.generatePresignedUploadUrl(this.bucket, key, Duration.ofMinutes(15));
        LOG.info("Presigned URL for course trailer generated successfully.");
        return new TrailerUploadResponse(
                url.toString(),
                key,
                LocalDateTime.now().plus(Duration.ofMinutes(15)));
    }

    private void validateVideoType(String contentType) {
        if (contentType == null || !ALLOWED_VIDEO__TYPES.contains(contentType)) {
            throw new BadRequestException("Invalid video content type.");
        }
    }

    private void validateFileSize(long size) {
        if (size > MAXIMUM_FILE_SIZE_BYTES) {
            throw new BadRequestException("File size is too large");
        }
    }

}
