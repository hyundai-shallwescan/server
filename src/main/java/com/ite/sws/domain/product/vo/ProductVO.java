package com.ite.sws.domain.product.vo;

import java.util.Date;

import lombok.Data;

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
 * </pre>
 */

@Data
public class ProductVO {
	private Long productId;
	private int price;
	private String thumbnailImage;
	private String descriptionImage;
	private String barcode;
	private String description;
	private Date createdAt;
	private Date updatedAt;
}