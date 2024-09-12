package com.ite.sws.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 완료 DTO
 * @author 김민정
 * @since 2024.09.11
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.11  	김민정       최초 생성
 * </pre>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDoneDTO {
    private Long paymentId;
    private Long oldCartId;
    private Long newCartId;
    private String cartOwnerName;
    private String qrUrl;
    @Builder.Default
    private String type = "paymentDone";   // "cartUpdate", "paymentDone"
}
