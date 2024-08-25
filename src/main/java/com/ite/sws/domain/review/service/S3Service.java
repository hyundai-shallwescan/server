package com.ite.sws.domain.review.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.File;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * Review관련 S3 service
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

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3 amazonS3;


    public String getPreAssignedUrl(String keyName, String bucket) {
        return amazonS3.getUrl(bucket, keyName).toString();
    }

    public void uploadFile(String keyName, String bucket, File file) {
        try {
            PutObjectRequest request = new PutObjectRequest(bucket, keyName, file);
            amazonS3.putObject(request);
        } catch (AmazonServiceException e) {
            throw new AmazonS3Exception("S3에서 파일 업로드가 실패했습니다.");
        }
    }

    public void deleteFile(String keyName, String bucket) {
        try {
            DeleteObjectRequest request = new DeleteObjectRequest(bucket, keyName);
            amazonS3.deleteObject(request);
        } catch (AmazonServiceException e) {
            throw new AmazonS3Exception("S3에서 파일 삭제 실패했습니다.");
        }
    }

    public InputStream downloadFile(String bucket, String keyName) {
        S3Object s3Object = amazonS3.getObject(bucket, keyName);
        return s3Object.getObjectContent();
    }
}
