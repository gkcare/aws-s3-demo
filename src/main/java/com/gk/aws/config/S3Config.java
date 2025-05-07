package com.gk.aws.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3CrtAsyncClientBuilder;
import software.amazon.awssdk.services.s3.internal.crt.S3CrtAsyncClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class S3Config {

    private String region;
    private S3Properties s3;

  //  private S3Client

    @Setter @Getter
    public static class S3Properties{
        private String bucketName;
        private String accessKey;
        private String secretKey;
    }

    @Bean
    public S3AsyncClient s3AsyncClient() {
        AwsBasicCredentials creds = AwsBasicCredentials.create(s3.getAccessKey(),s3.getSecretKey());
        /*S3CrtAsyncClientBuilder builder = S3CrtAsyncClient.builder().region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .targetThroughputInGbps(10.0)
                .minimumPartSizeInBytes(8*1024*1024L);*/

        return S3AsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();

    }

    @Bean
    public ExecutorService s3Executor() {
        return Executors.newFixedThreadPool(5);
    }

}
