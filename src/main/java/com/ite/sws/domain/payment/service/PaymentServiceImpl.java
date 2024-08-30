package com.ite.sws.domain.payment.service;

import com.ite.sws.domain.payment.dto.PostPaymentReq;
import com.ite.sws.domain.payment.dto.PostPaymentRes;
import com.ite.sws.domain.payment.mapper.PaymentMapper;
import com.ite.sws.domain.payment.vo.PaymentVO;
import com.ite.sws.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.ite.sws.exception.ErrorCode.CART_ITEM_NOT_FOUND;
import static com.ite.sws.exception.ErrorCode.CART_NOT_FOUND;
import static com.ite.sws.exception.ErrorCode.DATABASE_ERROR;

/**
 * 상품 결제 서비스 구현체
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
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final QRCodePersistenceHelper qrCodePersistenceHelper;

    /**
     * 상품 결제 생성
     * @param postPaymentReq 상품 결제 객체
     */
    @Override
    @Transactional
    public PostPaymentRes addPayment(PostPaymentReq postPaymentReq) {

        // 결제 시각: KST -> UTC 변환
        ZonedDateTime utcZonedDateTime = ZonedDateTime
                .parse(postPaymentReq.getPaymentTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.of("Asia/Seoul")))
                .withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime utcLocalDateTime = utcZonedDateTime.toLocalDateTime();

        // 1. 상품 결제 정보 삽입을 위한 프로시저 호출
        // 1-0. 장바구니 존재 확인
        // 1-1. 결제 생성
        // 1-2. 결제 아이템 생성
        // 1-3. 현재 장바구니 상태 DONE + URI NULL
        PaymentVO newPayment = PaymentVO.builder()
                .cartId(postPaymentReq.getCartId())
                .amount(postPaymentReq.getTotalPrice())
                .paymentCard(postPaymentReq.getCard())
                .paymentKey(postPaymentReq.getPaymentKey())
                .paymentTime(utcLocalDateTime)
                .build();

        try {
            paymentMapper.insertPayment(newPayment);
        } catch (UncategorizedSQLException e) {
            if (e.getSQLException().getErrorCode() == 20001) {
                throw new CustomException(CART_NOT_FOUND);
            } else if (e.getSQLException().getErrorCode() == 20002) {
                throw new CustomException(CART_ITEM_NOT_FOUND);
            }
            // 다른 예외 처리
            throw new CustomException(DATABASE_ERROR);
        }

        // 2. QR 코드 생성 및 S3 저장
        String paymentId = String.valueOf(newPayment.getPaymentId());
        String qrText = generateQRText(paymentId);
        String qrCodeUri = qrCodePersistenceHelper.uploadQRCode(qrText, paymentId);

        // 3. 장바구니 및 QR 코드 생성을 위한 프로시저 호출
        // 3-1. 이전 장바구니를 가졌던 유저에게, 새로운 장바구니 생성
        // 3-2. QR 코드 저장, 반환
        // TODO: 장바구니 URI 암호화
        paymentMapper.insertCartAndQRCode(postPaymentReq.getCartId(), newPayment.getPaymentId(), qrCodeUri);
        return PostPaymentRes.builder()
                .paymentId(newPayment.getPaymentId())
                .qrUrl(qrCodeUri)
                .build();
    }

    // QR 텍스트 생성
    private String generateQRText(String qrText) {
        return "scanandgo://deeplink?paymentId=" + qrText;
    }
}
