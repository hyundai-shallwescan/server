package com.ite.sws.domain.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 주차 정산 정보 조회 응답 DTO
 * @author 남진수
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	남진수       최초 생성
 * </pre>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetParkingRes {
    private LocalDateTime entranceAt; // 입차 시간
    private String carNumber; // 차량 번호
    private Long discountParkingHour; // 할인 주차 시간(시)
    private String parkingPaymentStatus; // EMPTY, CART, PAID
    private Long parkingHour; // 주차 시간(시)
    private Long parkingMinute; // 주차 시간(분)
    private Long paymentHour; // 정산 필요 주차 시간(시)
    private Long paymentMinute; // 정산 필요 주차 시간(분)
    private Long parkingFee; // 주차 정산 요금
}