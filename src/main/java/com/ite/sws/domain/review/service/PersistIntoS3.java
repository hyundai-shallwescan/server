package com.ite.sws.domain.review.service;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * 미디어 파일 저장소 S3 구체화 클래스
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	구지웅      최초 생성
 * </pre>
 *
 */
@RequiredArgsConstructor
@Service
public class PersistIntoS3 implements
    MediaPersistLocationStrategy {

  private final S3Service s3Service;


  @Override
  public String getPreAssignedUrl(String fileName, String bucket) {
    return s3Service.getPreAssignedUrl(fileName,bucket);
  }

  @Override
  public void save(String fileName, String bucketName, File file) {
    s3Service.uploadFile(fileName, bucketName, file);
  }

  @Override
  public void remove(String fileName, String bucket) {
    s3Service.deleteFile(fileName,bucket);
  }
}
