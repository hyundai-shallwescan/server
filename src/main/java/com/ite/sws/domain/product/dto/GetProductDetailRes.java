package com.ite.sws.domain.product.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetProductDetailRes {
  private Long productId;
  private Long price;
  private String name;
  private String thumbnailImage;
  private String descriptionImage;
  private String barcode;
  private String description;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
