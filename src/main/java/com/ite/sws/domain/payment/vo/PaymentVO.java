package com.ite.sws.domain.payment.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 상품 결제 VO
 * @author 김민정
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	김민정       최초 생성
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PaymentVO {
    private Long paymentId;
    private Long cartId;
    private Integer amount;
    private String paymentCard;
    private String paymentKey;
    private LocalDateTime paymentTime;
    private List<PaymentItemVO> items;
}
