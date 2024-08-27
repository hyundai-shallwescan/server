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
  private Long productId;
  private String shortFormUrl;
  private String shortFormThumbnailImage;
  private LocalDateTime createdAt;
  private Long price;
  private String name;
  private String productThumbnailImage;
  private String descriptionImage;
  private String barcode;
  private String description;
}
