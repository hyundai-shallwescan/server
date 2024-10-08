package com.ite.sws.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 결제 Response DTO
 * @author 김민정
 * @since 2024.08.30
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.30  	김민정       최초 생성
 * 2024.09.01  	김민정       새로운 cartId 필드 추가
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostPaymentRes {
    private Long paymentId;
    private Long newCartId;
    private String qrUrl;
}
