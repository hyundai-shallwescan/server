package com.ite.sws.domain.product.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 VO
 * @author 정은지
 * @since 2024.08.23
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.23  	정은지       최초 생성
 * 2024.8.26 		구지웅				name 추가
 * </pre>
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductVO {
	private Long productId;
	private Long price;
	private String name;
	private String thumbnailImage;
	private String descriptionImage;
	private String barcode;
	private String description;
}