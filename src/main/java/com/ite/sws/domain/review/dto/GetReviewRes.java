package com.ite.sws.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

