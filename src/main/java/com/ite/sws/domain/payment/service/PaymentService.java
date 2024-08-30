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
 * 2024.08.28  	김민정       상품 결제 생성 기능 추가
 * </pre>
 */
public interface PaymentService {
    PostPaymentRes addPayment(PostPaymentReq postPaymentReq);
}
