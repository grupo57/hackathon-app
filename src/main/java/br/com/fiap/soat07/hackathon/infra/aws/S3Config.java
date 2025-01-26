package br.com.fiap.soat07.hackathon.infra.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Autowired
    private AwsConfig awsConfig;

    @Value("${cloud.aws.s3.region}")
    private String region;

    @Bean(name = "s3Region")
    public Region s3Region() {
        return Region.of(region);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(s3Region())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsConfig.getAccessKey(), awsConfig.getSecretKey())
                ))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(s3Region())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsConfig.getAccessKey(), awsConfig.getSecretKey())
                ))
                .build();
    }
}