package com.ite.sws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Review관련 S3 configuration
 *
 * @author 구지웅
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	구지웅      최초 생성
 * </pre>
 * @since 2024.08.24
 */
@Configuration
public class S3Config {
   @Value("${cloud.aws.credentials.access-key}")
   private String accessKey;
   @Value("${cloud.aws.credentials.secret-key}")
   private String secretKey;
   @Value("${cloud.aws.region.static}")
   private String region;


   @Qualifier("amazonReviewS3Client")
   @Bean
   public AmazonS3Client amazonReviewS3Client() {
      BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
      return (AmazonS3Client)AmazonS3ClientBuilder.standard()
         .withRegion(region)
         .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
         .build();
   }
}
