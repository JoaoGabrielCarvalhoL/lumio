package br.com.joaogabriel.lumio.messaging;

import br.com.joaogabriel.lumio.model.dto.messaging.S3UploadEventMessage;

public interface MediaUploadProcessor {

    boolean isApplicable(String bucket);

    void process(S3UploadEventMessage s3UploadEventMessage);
}
