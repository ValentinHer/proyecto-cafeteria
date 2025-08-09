package com.valentin.reservacion_citas.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3ClientConfig {
	@Value("${aws.bucket-region}")
	private String region;

	@Value("${aws.person-landing-page.access-key}")
	private String accessKey;

	@Value("${aws.person-landing-page.secret-key}")
	private String secretKey;

	@Bean
	public S3Client s3Client() {
		return S3Client.builder()
				.region(Region.of(region))
				.credentialsProvider(StaticCredentialsProvider.create(getAwsCredentials()))
				.build();
	}

	@Bean
	public S3Presigner s3Presigner(){
		return S3Presigner.builder()
						  .region(Region.of(region))
						  .credentialsProvider(StaticCredentialsProvider.create(getAwsCredentials()))
						  .build();
	}

	private AwsBasicCredentials getAwsCredentials() {
		return AwsBasicCredentials.create(accessKey, secretKey);
	}
}
