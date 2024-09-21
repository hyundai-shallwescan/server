package com.ite.sws.domain.admin.dto;


import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Product를 업데이트하기 위한 Dto
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	구지웅      최초 생성
 * </pre>
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PatchProductReq {

  @NotNull
  private String name;
  @NotNull
  private Long price;
  @NotNull
  private String barcode;
  private String description;
}
