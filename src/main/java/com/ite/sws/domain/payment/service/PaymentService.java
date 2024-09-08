package com.ite.sws.domain.payment.service;

import com.ite.sws.domain.payment.dto.GetProductRecommendationRes;
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
 * 2024.08.28  	김민정       무료 주차 정산 가능 금액대 상품 조회
 * 2024.09.08  	김민정       주차 시간에 따른 필요한 최소 구매 금액을 결정
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

    /**
     * 무료 주차 정산 가능 금액대 상품 조회
     * @param cartId 장바구니 ID
     * @return
     */
    GetProductRecommendationRes findRecommendProduct(Long cartId);

    /**
     * 주차 시간에 따른 필요한 최소 구매 금액을 결정
     * @param parkedMinutes 주차 시간 (분)
     * @return 필요한 최소 구매 금액 (원)
     */
    int determineRequiredPurchaseAmount(long parkedMinutes);
}
