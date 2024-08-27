package com.ite.sws.domain.admin.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.lang.Nullable;


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
  private String barcode;
  private String description;
}