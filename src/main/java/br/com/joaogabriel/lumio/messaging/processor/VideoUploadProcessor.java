package br.com.joaogabriel.lumio.messaging.processor;

import br.com.joaogabriel.lumio.messaging.MediaUploadProcessor;
import br.com.joaogabriel.lumio.model.dto.messaging.S3UploadEventMessage;
import br.com.joaogabriel.lumio.service.LessonVideoService;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class VideoUploadProcessor implements MediaUploadProcessor {

    private final LessonVideoService lessonVideoService;
    private final String bucket;
    private static final Logger LOG = LoggerFactory.getLogger(VideoUploadProcessor.class.getName());

    public VideoUploadProcessor(LessonVideoService lessonVideoService,
                                @ConfigProperty(name = "S3_BUCKET_VIDEOS") String bucket) {
        this.lessonVideoService = lessonVideoService;
        this.bucket = bucket;
    }

    @Override
    public boolean isApplicable(String bucket) {
        return bucket.equals(this.bucket);
    }

    @Override
    public void process(S3UploadEventMessage s3UploadEventMessage) {
        this.lessonVideoService.processVideoActivation(s3UploadEventMessage.key(), s3UploadEventMessage.size());
    }
}
