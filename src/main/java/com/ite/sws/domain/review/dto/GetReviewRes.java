package com.ite.sws.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Review를 얻기 위한 DTO
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
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetReviewRes {
  private Long shortFormId;
  private Long productId;
  private String shortFormUrl;
  private String thumbnailImage;
}

