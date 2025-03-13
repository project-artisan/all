package org.artisan.infra.s3;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private static final Logger log = LoggerFactory.getLogger(ImageUploadService.class);
    private final S3Client s3Client;
    private final AwsProperty awsProperty;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Result uploadByUrl(ImageUploadRequest request) {
        log.debug("uploaded image url {}", request.imageUrl());

        var connection = getConnection(request.imageUrl());

        if(connection == null){
            applicationEventPublisher.publishEvent(new UploadFailEvent(request));
            return Result.FAIL;
        }

        try (InputStream inputStream = connection.getInputStream()) {
            var contentLength = connection.getContentLengthLong();
            var contentType = connection.getContentType();
            var putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsProperty.bucketName())
                    .key(request.imageIdentifier().key())
                    .contentType(contentType)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));
            return Result.OK;
        } catch (Exception e) {
            applicationEventPublisher.publishEvent(new UploadFailEvent(request));
            return Result.FAIL;
        }
    }

    private URLConnection getConnection(String imageUrl) {
        if(Strings.isBlank(imageUrl)){
            return null;
        }
        try {
            var url = new URL(imageUrl);
            return url.openConnection();
        } catch (IOException e) {
            return null;
        }
    }

    public enum Result {
        OK,
        FAIL
    }


}
