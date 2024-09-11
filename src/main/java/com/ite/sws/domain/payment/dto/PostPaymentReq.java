package com.ite.sws.domain.payment.dto;

import com.ite.sws.domain.payment.vo.PaymentItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 결제 Request DTO
 * @author 김민정
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	김민정       최초 생성
 * 2024.09.09   김민정       결제 아이템 리스트 필드 추가
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostPaymentReq {
    private Long cartId;
    private Integer totalPrice;
    private String card;
    private String paymentKey;
    private String paymentTime;
    private List<PaymentItemVO> items;
}
