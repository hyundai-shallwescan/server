package com.ite.sws.domain.product.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 상품의 디테일을 얻을 수 있는 DTO
 * @author 구지웅
 * @since 2024.08.27
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  	구지웅        최초 생성
 * </pre>
 */
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
