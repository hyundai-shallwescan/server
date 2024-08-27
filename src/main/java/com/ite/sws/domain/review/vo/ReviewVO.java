package com.ite.sws.domain.review.vo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
