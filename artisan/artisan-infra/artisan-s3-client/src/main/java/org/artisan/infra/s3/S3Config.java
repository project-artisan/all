package org.artisan.infra.s3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    private final AwsCredentialsProvider awsCredentialsProvider;

    public S3Config(AwsProperty awsProperty) {
        var awsBasicCredentials = AwsBasicCredentials.create(
                awsProperty.accessKey(),
                awsProperty.secretKey()
        );

        this.awsCredentialsProvider = StaticCredentialsProvider.create(
                awsBasicCredentials
        );
    }

    @Bean
    public S3Client amazonS3Client() {
        return S3Client.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.AP_NORTHEAST_2)
                .build();
    }

    @Bean
    public S3Presigner preSigner() {
        return S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }
}
