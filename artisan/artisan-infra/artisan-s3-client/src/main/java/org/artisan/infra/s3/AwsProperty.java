package org.artisan.infra.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationProperties("aws")
@ConfigurationPropertiesBinding
public record AwsProperty(
        String accessKey,
        String secretKey,
        String bucketName,
        String cloudFrontUrl
) {


}