package br.com.joaogabriel.lumio.messaging.processor;

import br.com.joaogabriel.lumio.messaging.MediaUploadProcessor;
import br.com.joaogabriel.lumio.model.dto.messaging.S3UploadEventMessage;
import br.com.joaogabriel.lumio.service.CourseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TrailerUploadProcessor implements MediaUploadProcessor {

    private final CourseService courseService;
    private final String bucket;

    public TrailerUploadProcessor(CourseService courseService,
                                  @ConfigProperty(name = "S3_BUCKET_VIDEOS") String bucket) {
        this.courseService = courseService;
        this.bucket = bucket;
    }

    @Override
    public boolean isApplicable(String bucket) {
        return bucket.equals(this.bucket);
    }

    @Override
    @Transactional(Transactional.TxType.MANDATORY)
    public void process(S3UploadEventMessage s3UploadEventMessage) {
        this.courseService.processTrailerActivation(s3UploadEventMessage.key(), s3UploadEventMessage.size());
    }
}
