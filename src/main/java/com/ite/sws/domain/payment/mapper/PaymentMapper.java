package com.ite.sws.domain.payment.mapper;

import com.ite.sws.domain.payment.vo.PaymentVO;
import org.apache.ibatis.annotations.Param;

/**
 * 상품 결제 매퍼 인터페이스
 * @author 김민정
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	김민정       최초 생성
 * 2024.08.28  	김민정       상품 결제 정보 삽입
 * 2024.08.28  	김민정       새로운 장바구니 및 인증 QR 정보 삽입을 위한 프로시저 호출
 * </pre>
 */
public interface PaymentMapper {

    /**
     * 상품 결제 정보 삽입
     * @param newPayment 상품 결제 생성 객체
     */
    void insertPayment(PaymentVO newPayment);

    /**
     * 새로운 장바구니 및 인증 QR 정보 삽입을 위한 프로시저 호출
     * @param cartId 기존 장바구니 ID
     * @param paymentId 결제 ID
     * @param qrCodeUri QR 코드 URI
     */
    void insertCartAndQRCode(@Param("cartId") Long cartId,
                             @Param("paymentId") Long paymentId,
                             @Param("qrCodeUri") String qrCodeUri);
}
