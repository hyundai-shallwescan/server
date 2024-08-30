package com.ite.sws.domain.payment.controller;

import com.ite.sws.domain.payment.dto.PostPaymentReq;
import com.ite.sws.domain.payment.dto.PostPaymentRes;
import com.ite.sws.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품 결제 컨트롤러
 * @author 김민정
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	김민정       최초 생성
 * 2024.08.28  	김민정       상품 결제 내역 등록 API 생성
 * </pre>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 상품 결제 내역 등록 API
     * @param postPaymentReq 결제 내역 객체
     * @return 결제 등록 결과 응답
     */
    @PostMapping
    public ResponseEntity<PostPaymentRes> addPayment(@RequestBody PostPaymentReq postPaymentReq) {
        return ResponseEntity.ok(paymentService.addPayment(postPaymentReq));
    }
}
