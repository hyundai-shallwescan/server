package com.ite.sws.domain.parking.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ParkingPayment VO
 * @author 남진수
 * @since 2024.09.17
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.17  	남진수       최초 생성
 * </pre>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingPaymentVO {
    private Long parkingPaymentId;
    private Long parkingHistoryId;
    private Long paymentId;
    private Long amount;
    private String paymentKey;
    private String paymentCard;
    private String status;
}
