package com.ite.sws.domain.parking.service;

import com.ite.sws.domain.parking.dto.*;
import com.ite.sws.domain.parking.mapper.ParkingMapper;
import com.ite.sws.domain.parking.vo.ParkingHistoryVO;
import com.ite.sws.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 주차 서비스 구현체
 * @author 남진수
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	남진수       최초 생성
 * 2024.08.28  	남진수       주차 기록 추가 메서드 추가
 * 2024.08.28  	남진수       주차 기록 수정 메서드 추가
 * 2024.08.28  	남진수       주차 정산 정보 조회 메서드 추가
 * 2024.08.28  	남진수       무료 주차 시간 계산 메서드 추가
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class ParkingServiceImpl implements ParkingService{

    private final ParkingMapper parkingMapper;
    private final PaymentService paymentService;

    /**
     * 주차 기록 추가(입차 처리)
     * @param postParkingReq 차량 번호
     */
    @Transactional
    public void addParkingHistory(PostParkingReq postParkingReq){
        Long memberId = parkingMapper.selectMemberIdByCarNumber(postParkingReq.getCarNumber());

        ParkingHistoryVO parkingHistoryVO = ParkingHistoryVO.builder()
                .memberId(memberId)
                .carNumber(postParkingReq.getCarNumber())
                .entranceAt(LocalDateTime.now())
                .build();
        parkingMapper.insertParkingHistory(parkingHistoryVO);
    }

    /**
     * 주차 기록 수정(출차 처리)
     * @param patchParkingReq 차량 번호
     */
    @Transactional
    public void modifyParkingHistory(PatchParkingReq patchParkingReq) {
        ParkingHistoryVO parkingHistoryVO = ParkingHistoryVO.builder()
                .carNumber(patchParkingReq.getCarNumber())
                .exitAt(LocalDateTime.now())
                .build();
        parkingMapper.updateParkingHistory(parkingHistoryVO);
    }

    /**
     * 주차 정산 정보 조회
     * @param memberId 회원 ID
     * @return GetParkingRes
     */
    public GetParkingRes findParkingInformation(Long memberId){

        ParkingHistoryDTO parkingHistoryDTO = parkingMapper.selectParkingHistoryByMemberId(memberId);
        // 현재 시간과 입차 시간의 차이(주차 시간)
        Duration parkingTime = Duration.between(parkingHistoryDTO.getEntranceAt(), LocalDateTime.now());

        int totalPrice;
        String parkingPaymentStatus;

        // 회원의 결제 금액 조회
        Integer paymentAmount = parkingMapper.selectPaymentAmountByMemberId(memberId);
        // 당일 결제 금액이 존재할 경우
        if (paymentAmount != null) {
            totalPrice = paymentAmount;
            parkingPaymentStatus = "PAID";
        } else {
            // 회원의 장바구니 목록 조회
            List<CartItemListDTO> cartItemList = parkingMapper.selectCartItemListByMemberId(memberId);

            // 장바구니 총 가격 계산
            totalPrice = cartItemList.stream()
                    .filter(item -> item.getIsDeleted() == 0)
                    .mapToInt(item -> item.getQuantity() * item.getPrice())
                    .sum();

            // 장바구니가 비어있을 경우
            if (totalPrice == 0) {
                parkingPaymentStatus = "EMPTY";
            } else {
                parkingPaymentStatus = "CART";
            }
        }

        // 영수증 할인 시간 계산
        Duration discountParkingDuration = calculateFreeParkingTimeToDurationType(totalPrice);
        Long discountParkingHours = discountParkingDuration.toHours();

        // 유료 주차 시간 계산 (총 주차 시간 - 무료 주차 시간)
        Duration paidParkingTime = parkingTime.minus(discountParkingDuration);

        // 무료로 출차 가능한 시간 계산
        long feeForFreeParking = 0L;
        String freeParkingTime = null;
        if (!parkingPaymentStatus.equals("EMPTY")) {
            if (paidParkingTime.isNegative()) {
                int freeParkingHour = (int) paidParkingTime.negated().toHours();
                int freeParkingMinute = (int) (paidParkingTime.negated().toMinutes() % 60);
                LocalDateTime freeParkingEndTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                        .plusHours(freeParkingHour)
                        .plusMinutes(freeParkingMinute);

                freeParkingTime = freeParkingEndTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                paidParkingTime = Duration.ZERO;
            } else {
                // 무료 출차를 위한 필요 금액 계산
                feeForFreeParking = paymentService.determineRequiredPurchaseAmount(paidParkingTime.toMinutes());
                // 무료 출차가 불가능한 경우
                if (feeForFreeParking == 0) {
                    freeParkingTime = "금액 정산 후 출차가 가능합니다.";
                }
            }
        }

        // 주차 요금 계산 (10분당 1,000원)
        Long parkingFee = (paidParkingTime.toMinutes() / 10 + (paidParkingTime.toMinutes() % 10 > 0 ? 1L : 0L)) * 1000L;

        return GetParkingRes.builder()
                .freeParkingTime(freeParkingTime)
                .feeForFreeParking(feeForFreeParking)
                .entranceAt(parkingHistoryDTO.getEntranceAt())
                .carNumber(parkingHistoryDTO.getCarNumber())
                .discountParkingHour(discountParkingHours)
                .parkingPaymentStatus(parkingPaymentStatus)
                .parkingHour(parkingTime.toHours())
                .parkingMinute(parkingTime.toMinutes() % 60)
                .paymentHour(paidParkingTime.toHours())
                .paymentMinute(paidParkingTime.toMinutes() % 60)
                .parkingFee(parkingFee)
                .build();
    }

    /**
     * 무료 주차 시간 계산(주차 정산 정책)
     * @param totalPrice 총 주문 금액
     * @return Duration
     */
    private Duration calculateFreeParkingTimeToDurationType(int totalPrice) {
        if (totalPrice >= 60000) {
            return Duration.ofHours(5); // 6만원 이상 구매 시 5시간
        } else if (totalPrice >= 40000) {
            return Duration.ofHours(3); // 4만원 이상 구매 시 3시간
        } else if (totalPrice >= 30000) {
            return Duration.ofHours(2); // 3만원 이상 구매 시 2시간
        } else if (totalPrice >= 20000) {
            return Duration.ofHours(1); // 2만원 이상 구매 시 1시간
        } else {
            return Duration.ZERO; // 그 외에는 무료 주차 없음
        }
    }

}
