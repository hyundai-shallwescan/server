package com.ite.sws.domain.payment.controller;

import com.ite.sws.domain.payment.dto.GetProductRecommendationRes;
import com.ite.sws.domain.payment.dto.PostPaymentReq;
import com.ite.sws.domain.payment.dto.PostPaymentRes;
import com.ite.sws.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
 * 2024.08.30  	김민정       QR 출입증 인증 API 생성
 * 2024.08.30  	김민정       무료 주차 정산 가능 금액대 상품 추천 API 생성
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

    /**
     * QR 출입증 인증 API
     * @param paymentId 결제 ID
     * @return QR 코드 인증 결과 응답
     */
    @PatchMapping("/{paymentId}")
    public ResponseEntity<Void> modifyExitCredentialStatus(@PathVariable Long paymentId) {
        paymentService.modifyExitCredentialStatus(paymentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 무료 주차 정산 가능 금액대 상품 추천 API
     * @param cartId 장바구니 ID
     * @param totalPrice 장바구니 총 금액
     * @return
     */
    @GetMapping("/carts/{cartId}/recommend")
    public ResponseEntity<GetProductRecommendationRes> findRecommendProduct(@PathVariable Long cartId,
                                                                            @RequestParam Long totalPrice) {
        return ResponseEntity.ok(paymentService.findRecommendProduct(cartId, totalPrice));
    }
}
