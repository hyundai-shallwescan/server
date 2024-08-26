package com.ite.sws.domain.admin.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostCreateProductReq {
  @NotNull
  private String name;
  @NotNull
  private Long price;
  @NotNull
  private String thumbnailImage;
  @NotNull
  private String descriptionImage;
  @NotNull
  private String barcode;
  @NotNull
  private String description;
}