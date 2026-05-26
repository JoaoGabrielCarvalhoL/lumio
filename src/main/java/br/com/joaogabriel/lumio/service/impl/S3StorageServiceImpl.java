package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.service.S3StorageService;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

@ApplicationScoped
public class S3StorageServiceImpl implements S3StorageService {
    private static final Logger LOG = LoggerFactory.getLogger(S3StorageServiceImpl.class);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    public S3StorageServiceImpl(S3Client s3Client,
                                S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    @Override
    public FileUploadResponse upload(FileUploadRequest request, String bucket, String key) {
        String filename = request.file().fileName();
        String contentType = request.file().contentType();
        LOG.info("Uploading file {} to Amazon S3.", filename);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .build();

        this.s3Client.putObject(putObjectRequest, RequestBody.fromFile(request.file().uploadedFile()));
        LOG.info("File uploaded successfully.");
        return new FileUploadResponse(filename, key, request.file().size(), contentType ,LocalDateTime.now());
    }

    @Override
    public Boolean exists(String bucket, String key) {
        this.s3Client.headObject(builder ->
                builder.bucket(bucket)
                        .key(key)
                        .build());
        return true;
    }

    @Override
    public URL generatePresignedUploadUrl(String bucket, String key, Duration duration) {
        LOG.info("Generating Presigned Upload URL for course trailer. Target Key: {}", key);
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket).key(key).build();

            PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(duration)
                    .putObjectRequest(putObjectRequest).build();

            return this.s3Presigner.presignPutObject(putObjectPresignRequest).url();

        } catch (Exception ex) {
            LOG.error("Failed to generate Presigned Upload URL for course trailer.", ex);
            throw new RuntimeException("Storage infrastructure error.", ex);
        }
    }

    @Override
    public URL generatePresignedDownloadUrl(String bucket, String key, Duration duration) {
        LOG.info("Generating Presigned Download URL for course trailer. Target Key: {}", key);

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket).key(key).build();

            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(duration).getObjectRequest(getObjectRequest).build();

            return this.s3Presigner.presignGetObject(getObjectPresignRequest).url();
        } catch (Exception ex) {
            LOG.error("Failed to generate Presigned Download URL for course trailer.", ex);
            throw new RuntimeException("Storage infrastructure error.", ex);
        }
    }

    @Override
    public void delete(String bucket, String key) {
        LOG.info("Deleting course trailer. Target Key: {}", key);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket).key(key).build();
        this.s3Client.deleteObject(deleteObjectRequest);
    }
}
