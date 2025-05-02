package com.hyyh.festa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;


@Configuration
public class S3Config {

    @Value("${cloud_aws_credentials_access_key}")
    private String accessKey;

    @Value("${cloud_aws_credentials_secret_key}")
    private String secretKey;

    @Value("${cloud_aws_region}")
    private String region;

    @Bean
    @Primary
    public AwsBasicCredentials awsCredentialsProvider() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentialsProvider()))
                .build();
    }
    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentialsProvider()))
                .build();
    }
}