package com.toostew.noteShare.config;


import com.toostew.noteShare.exception.pojo.R2ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AWSServiceClientSource {


    //these values should absolutely NOT be hardcoded in for security reasons dummy
    @Value("${Access.Key.ID}")
    private String accessKeyId;

    @Value("${Token.value}")
    private String tokenValue;

    @Value("${Secret.Access.Key}")
    private String secretAccessKey;


    //every aws service we want to access requires a singleton instance
    @Bean
    public S3Client getS3Client(){


        //NOTE: R2 Does not utilize session tokens because sessions are validated by AWS for
        //Identity and Access Management (IAM) and Security Token Service, both not used by R2
        //NOTE2: we specify the URI to point to cloudflare, following cloudflare convention
        try{
            System.out.println("DEBUG: creating s3Client with:");
            System.out.println("accessKeyId: " + accessKeyId);
            System.out.println("secretAccessKey: " + secretAccessKey);
            S3Client s3Client = S3Client.builder()
                    .region(Region.of("auto")) // set region to auto, per R2 specs
                    .endpointOverride( //set the endpoint to R2 from cloudflare
                            URI.create("https://6deeea551209efbc172e1f67a4033678.r2.cloudflarestorage.com"))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId,secretAccessKey)))
                    .build();

            return s3Client;
        }
        catch (AwsServiceException e){
            //issue from the R2 service itself
            throw new R2ServiceException("Failed to upload to R2, R2 Server issue");
        }
        catch (SdkClientException e) {
            //issue occured within the java client code
            throw new RuntimeException(e);
        }


    }

}
