package com.project.georadiusservice.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    @Value("${application.env}")
    private String environment;

    @Value("${cloud.aws.s3.endpoint}")
    private String endpoint;

    @Value("${s3.access.key}")
    private String s3AccessKey;

    @Value("${s3.secret.key}")
    private String s3SecretKey;


    @Bean
    public AmazonS3Client amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(s3AccessKey, s3SecretKey);
        String region = Regions.AP_SOUTH_1.getName();
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withClientConfiguration(getClientConfiguration())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    private ClientConfiguration getClientConfiguration() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        if (environment.equalsIgnoreCase("dev")) {
            clientConfiguration.setProtocol(Protocol.HTTP);
        } else {
            clientConfiguration.setProtocol(Protocol.HTTPS);
        }
        return clientConfiguration;
    }


}
