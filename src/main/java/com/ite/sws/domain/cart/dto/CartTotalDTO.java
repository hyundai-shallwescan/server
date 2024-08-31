package com.ite.sws.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 장바구니 총 금액 DTO
 * @author 김민정
 * @since 2024.08.30
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.30  	김민정       최초 생성
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CartTotalDTO {
    private Long memberId;
    private Long totalCartFee;  // 장바구니에 담긴 총 결제 예상 금액
}
