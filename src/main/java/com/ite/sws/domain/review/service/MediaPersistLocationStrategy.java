package com.ite.sws.domain.review.service;

import java.io.File;
/**
 * 미디어 파일 저장소 전략 클래스
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
public interface MediaPersistLocationStrategy {

  public String getPreAssignedUrl(String fileName, String bucket);

  public void save(String fileName, String bucketName, File file);

  public void remove(String fileName, String bucket);
}

