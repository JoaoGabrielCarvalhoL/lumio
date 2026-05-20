package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.service.S3StorageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

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
    @Transactional
    public FileUploadResponse upload(FileUploadRequest request, String bucket, String key) {
        String filename = request.file().fileName();
        String contentType = request.file().contentType();
        LOG.info("Uploading file {} to Amazon S3.", filename);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(request.file().fileName())
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
    public URL generatePresignedUrl(String bucket, String key, Duration duration) {
        return null;
    }

    @Override
    public void delete(String bucket, String key) {

    }
}
