package com.ite.sws.domain.payment.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CartQRCode VO
 * @author 김민정
 * @since 2024.09.01
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.01  	김민정       최초 생성
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CartQRCodeVO {
    private Long cartId;
    private Long paymentId;
    private String qrCodeUri;
    private Long newCartId;
}
