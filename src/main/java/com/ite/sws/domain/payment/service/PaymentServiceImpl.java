package com.ite.sws.domain.payment.service;

import static com.ite.sws.exception.ErrorCode.CART_ITEM_NOT_FOUND;
import static com.ite.sws.exception.ErrorCode.CART_NOT_FOUND;
import static com.ite.sws.exception.ErrorCode.DATABASE_ERROR;
import static com.ite.sws.exception.ErrorCode.EXIT_CREDENTIAL_NOT_FOUND;

import com.ite.sws.domain.admin.dto.PaymentEvent;
import com.ite.sws.domain.admin.service.WebFluxAsyncPaymentInfoEventPublisher;
import com.ite.sws.domain.cart.mapper.CartMapper;
import com.ite.sws.domain.member.mapper.MemberMapper;
import com.ite.sws.domain.parking.dto.ParkingHistoryDTO;
import com.ite.sws.domain.parking.mapper.ParkingMapper;
import com.ite.sws.domain.payment.dto.GetProductRecommendationRes;
import com.ite.sws.domain.payment.dto.PaymentDoneDTO;
import com.ite.sws.domain.payment.dto.PostPaymentReq;
import com.ite.sws.domain.payment.dto.PostPaymentRes;
import com.ite.sws.domain.payment.event.PaymentDoneEvent;
import com.ite.sws.domain.payment.mapper.PaymentMapper;
import com.ite.sws.domain.payment.vo.CartQRCodeVO;
import com.ite.sws.domain.payment.vo.PaymentItemVO;
import com.ite.sws.domain.payment.vo.PaymentVO;
import com.ite.sws.domain.product.vo.ProductVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.util.SecurityUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * 2024.08.28  	김민정       출입증 인증 처리 기능 추가
 * 2024.08.28  	김민정       무료 주차 정산 가능 금액대 상품 조회
 * 2024.08.30  	김민정       주차 시간에 따른 주차 요금 계산
 * 2024.08.28  	김민정       주차 시간에 따른 필요한 최소 구매 금액을 결정
 * 2024.08.28  	김민정       추가 구매가 필요한 금액에 가장 가까운 상품을 조회
 * 2024.09.01  	김민정       결제 후, 새로운 장바구니 ID 반환 기능 추가
 * 2024.09.02 	구지웅       결제 이벤트 발생 시 결제 이벤트 발행
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final CartMapper cartMapper;
    private final ParkingMapper parkingMapper;
    private final QRCodePersistenceHelper qrCodePersistenceHelper;
    private final WebFluxAsyncPaymentInfoEventPublisher eventPublisher;
    private final MemberMapper memberMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 상품 결제 생성
     *
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
        // 1-2. 현재 장바구니 상태 DONE + URI NULL
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
            handleDatabaseException(e);
        }

        // 2. 클라이언트로부터 받은 결제 아이템 정보를 이용해 결제 아이템 생성
        List<PaymentItemVO> paymentItems = postPaymentReq.getItems();
        paymentMapper.insertPaymentItems(newPayment.getPaymentId(), paymentItems);

        // 3. QR 코드 생성 및 S3 저장
        String paymentId = String.valueOf(newPayment.getPaymentId());
        String qrText = generateQRText(paymentId);
        String qrCodeUri = qrCodePersistenceHelper.uploadQRCode(qrText, paymentId);

        // 4. 장바구니 및 QR 코드 생성을 위한 프로시저 호출
        // 4-1. 이전 장바구니를 가졌던 유저에게, 새로운 장바구니 생성
        // 4-2. QR 코드 저장, 반환
        // 4-3. 장바구니 주인의 cart_member_id의 cart_id를 새로운 장바구니로 변경
        CartQRCodeVO cartQRCodeVO = CartQRCodeVO.builder()
            .cartId(postPaymentReq.getCartId())
            .paymentId(newPayment.getPaymentId())
            .qrCodeUri(qrCodeUri)
            .build();
        paymentMapper.insertCartAndQRCode(cartQRCodeVO);

       emitPaymentEvent(newPayment,utcLocalDateTime);

        // 결제 관련 이벤트 발행 (비동기 처리)
        PaymentDoneDTO paymentDoneDTO = PaymentDoneDTO.builder()
                .paymentId(newPayment.getPaymentId())
                .oldCartId(postPaymentReq.getCartId())
                .newCartId(cartQRCodeVO.getNewCartId())
                .cartOwnerName(cartQRCodeVO.getCartOwnerName())
                .qrUrl(qrCodeUri)
                .build();
        applicationEventPublisher.publishEvent(new PaymentDoneEvent(this, paymentDoneDTO));

        return PostPaymentRes.builder()
            .paymentId(newPayment.getPaymentId())
            .newCartId(cartQRCodeVO.getNewCartId())
            .qrUrl(qrCodeUri)
            .build();
    }
    private void emitPaymentEvent(PaymentVO paymentVO,LocalDateTime currentTime){
          eventPublisher.emitPaymentEvent(
            PaymentEvent.of(paymentVO.getPaymentId(), cartMapper.selectMemberIdByCartId(paymentVO.getCartId()),
                memberMapper.selectMemberByMemberId(SecurityUtil.getCurrentMemberId()).getName(),currentTime));
    }

    // QR 텍스트 생성
    private String generateQRText(String qrText) {
        return "wlstnam.github.io/?paymentId=" + qrText;
    }

    /**
     * 데이터베이스 에외 핸들링
     * @param e
     */
    private void handleDatabaseException(UncategorizedSQLException e) {
        if (e.getSQLException().getErrorCode() == 20001) {
            throw new CustomException(CART_NOT_FOUND);
        } else if (e.getSQLException().getErrorCode() == 20002) {
            throw new CustomException(CART_ITEM_NOT_FOUND);
        }
        throw new CustomException(DATABASE_ERROR);
    }

    /**
     * 출입증 인증 처리
     * @param paymentId 결제 ID
     */
    @Override
    @Transactional
    public void modifyExitCredentialStatus(Long paymentId) {
        int rowsUpdated = paymentMapper.updateExitCredential(paymentId);

        if (rowsUpdated == 0) {
            throw new CustomException(EXIT_CREDENTIAL_NOT_FOUND);
        }
    }

    /**
     * 무료 주차 정산 가능 금액대 상품 조회
     * @param cartId 장바구니 ID
     * @param totalPrice 장바구니 총 금액
     * @return
     */
    @Override
    @Transactional
    public GetProductRecommendationRes findRecommendProduct(Long cartId, Long totalPrice) {

        // 1. 장바구니 총 금액 조회
        Long memberId = cartMapper.selectMemberIdByCartId(cartId);
        Long totalCartValue = totalPrice;

        // 예상 결제 금액이 60,000원을 초과하면 추천하지 않음
        if (totalCartValue > 60000) {
            return GetProductRecommendationRes.builder()
                    .message("예상 결제 금액이 60,000원을 초과하여 추천할 필요가 없습니다.").build();
        }

        // 2. 주차 기록 조회
        ParkingHistoryDTO parkingHistoryDTO = parkingMapper.selectParkingHistoryByMemberId(memberId);
        if (parkingHistoryDTO == null) {
            return GetProductRecommendationRes.builder()
                    .message("주차 기록이 없습니다.").build();
        }

        // 3. 주차 시간 계산(분 단위)
        Duration parkingTime = Duration.between(parkingHistoryDTO.getEntranceAt(), LocalDateTime.now());  // 주차 시간
        long parkedMinutes  = parkingTime.toMinutes();
        if (parkedMinutes  <= 0) {
            return GetProductRecommendationRes.builder()
                    .message("유효한 주차 시간이 아닙니다.").build();
        }

        // 4. 주차 요금 계산
        long parkingFee = calculateParkingFee(parkedMinutes);

        // 5. 무료 주차 시간을 결정하기 위한 최소 구매 금액 계산
        int requiredPurchaseAmount = determineRequiredPurchaseAmount(parkedMinutes);
        if (requiredPurchaseAmount == 0) {
            return GetProductRecommendationRes.builder()
                    .message("무료 주차가 필요하지 않습니다.").build();
        }

        // 6. 필요한 구매 금액과 현재 구매 금액 비교
        if (totalCartValue >= requiredPurchaseAmount) {
            // 이미 필요한 구매 금액을 충족하였으므로 추천하지 않음
            return GetProductRecommendationRes.builder()
                    .message("이미 필요한 구매 금액을 충족하였습니다.").build();
        }

        // 7. 추가로 구매해야 할 금액 계산
        long additionalAmountRequired = requiredPurchaseAmount - totalCartValue;

        // 8. 추가 금액에 해당하는 상품 추천
        ProductVO recommendedProduct = findProductsInPriceRange(additionalAmountRequired, memberId);
        if (recommendedProduct == null) {
            return GetProductRecommendationRes.builder()
                    .message("추가 구매에 적합한 상품을 찾을 수 없습니다.").build();
        }

        return GetProductRecommendationRes.builder()
                .message("추가 구매를 통해 무료 주차 혜택을 받을 수 있습니다.")
                .remainingParkingFee(additionalAmountRequired)
                .productId(recommendedProduct.getProductId())
                .productName(recommendedProduct.getName())
                .productPrice(recommendedProduct.getPrice())
                .thumbnailImage(recommendedProduct.getThumbnailImage())
                .build();
    }

    /**
     * 주차 시간에 따른 주차 요금 계산
     * @param parkedMinutes 주차 시간 (분)
     * @return 주차 요금 (원)
     */
    private int calculateParkingFee(long parkedMinutes) {
        return (int) Math.ceil(parkedMinutes / 10.0) * 1000;
    }

    /**
     * 주차 시간에 따른 필요한 최소 구매 금액을 결정
     * @param parkedMinutes 주차 시간 (분)
     * @return 필요한 최소 구매 금액 (원)
     */
    public int determineRequiredPurchaseAmount(long parkedMinutes) {
        // 무료 주차 시간 기준 (분 단위)
        final int ONE_HOUR = 60;
        final int TWO_HOURS = 120;
        final int THREE_HOURS = 180;
        final int FIVE_HOURS = 300;

        if (parkedMinutes <= ONE_HOUR) {
            return 20000;
        } else if (parkedMinutes <= TWO_HOURS) {
            return 30000;
        } else if (parkedMinutes <= THREE_HOURS) {
            return 40000;
        } else if (parkedMinutes <= FIVE_HOURS) {
            return 60000;
        } else {
            // 5시간 초과 시, 무료 주차 혜택을 더 이상 제공하지 않음
            return 0;
        }
    }

    /**
     * 추가 구매가 필요한 금액에 가장 가까운 상품을 조회
     * @param targetAmount 필요한 추가 구매 금액 (원)
     * @param memberId 추천 상품
     * @return
     */
    private ProductVO findProductsInPriceRange(long targetAmount, Long memberId) {
        // 1. 멤버의 이전 구매 기록을 확인하여 비슷한 가격대의 상품을 찾는다.
        ProductVO previousPurchase = paymentMapper.findPreviousPurchasesInPriceRange(memberId, targetAmount);

        if (previousPurchase != null) {
            // 2. 이전 구매 기록이 있으면 그 중 하나를 추천
            return previousPurchase;
        } else {
            // 3. 이전 구매 기록이 없으면 전체 상품 중에서 비슷한 가격대의 제품을 추천
            return paymentMapper.findSingleProductInPriceRange(targetAmount);
        }
    }

}
