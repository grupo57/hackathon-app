package br.com.fiap.soat07.hackathon.infra.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

    @Autowired
    private AwsConfig awsConfig;

    @Value("${cloud.aws.sqs.region}")
    private String region;

    @Value("${cloud.aws.sqs.video-processado-queue-name}")
    private String videoProcessadoQueueName;

    @Value("${cloud.aws.sqs.video-processado-queue-name}")
    private String videoErroQueueName;


    @Bean(name = "sqsRegion")
    public Region sqsRegion() {
        return Region.of(region);
    }

    public String getVideoProcessadoQueueName() {
        return videoProcessadoQueueName;
    }

    public String getVideoErroQueueName() {
        return videoErroQueueName;
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(sqsRegion())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsConfig.getAccessKey(), awsConfig.getSecretKey())
                ))
                .build();
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(sqsRegion())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsConfig.getAccessKey(), awsConfig.getSecretKey())
                ))
                .build();
    }
}