package com.ite.sws.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * Review를 생성하기위한 DTO
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

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateReviewReq {
  private Long paymentItemId;
  private Long productId;
}
