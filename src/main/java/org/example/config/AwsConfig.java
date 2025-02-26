package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;

/**
 * AWS SDK Configuration for IAM service.
 * <p>
 * Uses the default AWS credential provider chain, which checks for credentials in the following order:
 * 1. **Environment Variables** (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`)
 * 2. **Java System Properties** (`aws.accessKeyId`, `aws.secretAccessKey`)
 * 3. **AWS Profile (~/.aws/credentials)**
 * 4. **EC2 Instance Role / ECS Task Role**
 * <p>
 * If no credentials are found, the request will fail.
 */
@Configuration
public class AwsConfig {

    /**
     * Creates an AWS IAM client bean.
     * Uses the **default AWS credentials provider** (configured via CLI or instance role).
     *
     * @return Configured {@link IamClient} instance.
     */
    @Bean
    public IamClient iamClient() {
        return IamClient.builder()
                .region(Region.AWS_GLOBAL)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}
