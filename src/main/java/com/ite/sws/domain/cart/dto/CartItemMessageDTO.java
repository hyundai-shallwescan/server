package com.ite.sws.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 장바구니 아이템 상세 DTO
 * @author 김민정
 * @since 2024.09.05
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.05  	김민정       최초 생성
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CartItemMessageDTO {
    private Long cartId;
    private Long productId;
    private Integer quantity;
    private String productName;
    private Integer productPrice;
    private String productThumbnail;
    @Setter
    private String action; // "create", "increase", "decrease", "delete"
}
