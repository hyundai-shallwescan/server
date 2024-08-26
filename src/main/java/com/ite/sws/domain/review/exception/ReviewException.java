package com.ite.sws.domain.review.exception;
/**
 * Review관련 Exception을 다루기 위한 RuntimeException의 하위 Exception
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
public class ReviewException extends RuntimeException {
  public ReviewException(ReviewErrorCode errorCode) {
    super(errorCode.name());
  }
}
