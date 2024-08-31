package com.ite.sws.domain.payment.service;

import com.ite.sws.domain.payment.dto.PostPaymentReq;
import com.ite.sws.domain.payment.dto.PostPaymentRes;

/**
 * 상품 결제 서비스
 * @author 김민정
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	김민정       최초 생성
 * 2024.08.28  	김민정       상품 결제 기능 추가
 * 2024.08.28  	김민정       출입증 인증 기능 추가
 * </pre>
 */
public interface PaymentService {
    /**
     * 상품 결제
     * @param postPaymentReq 결제 내역 객체
     * @return
     */
    PostPaymentRes addPayment(PostPaymentReq postPaymentReq);

    /**
     * 출입증 인증
     * @param paymentId 결제 ID
     */
    void modifyExitCredentialStatus(Long paymentId);
}
