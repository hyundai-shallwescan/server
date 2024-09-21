package com.ite.sws.domain.review.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Review VO
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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReviewVO {

  private Long shortFormId;
  private Long paymentItemId;
  private Long productId;
  private String shortFormUrl;
  private String thumbnailImage;
  private LocalDateTime createdAt;
  private Integer isDeleted;
}
