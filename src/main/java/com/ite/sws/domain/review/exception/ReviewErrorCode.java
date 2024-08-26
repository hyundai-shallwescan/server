package com.ite.sws.domain.review.exception;

/**
 * Review의 ErrorCode를 다루기위한 Enum
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

public enum ReviewErrorCode {
  SYSTEM_ERROR(0000, "시스템 에러"),
  SYSTEM_DATABASE_ERROR(0001, "데이터베이스 커넥션 에러"),
  SYSTEM_DATABASE_INTEGRITY_ERROR(0002, "시스템 데이터베이스 무결성 에러"),
  REVIEW_IS_NOT_EXIST(1001, "리뷰가 존재하지 않습니다."),
  REVIEW_CANT_BE_PERSIST(1002, "리뷰를 저장할 수 없습니다. 데이터를 확인해주세요"),
  REVIEW_FILE_TYPE_NOT_PERMITTED(1003, "올바르지 않은 파일 형식입니다.");

  private final int code;
  private final String message;


  ReviewErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

}


