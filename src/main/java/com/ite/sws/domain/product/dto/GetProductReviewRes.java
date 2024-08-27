package com.ite.sws.domain.product.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetProductReviewRes {

  private Long shortFormId;
  private Long paymentItemId;
  private Long productId;
  private String shortFormUrl;
  private String thumbnailImage;
  private LocalDateTime createdAt;

}
