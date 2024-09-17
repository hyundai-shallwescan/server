package com.ite.sws.domain.parking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주차 결제 정보 등록 요청 DTO
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
public class PostParkingPaymentsReq {
    private Long parkingHistoryId;
    private Long paymentId;
    private Long amount;
    private String paymentKey;
    private String paymentCard;
}
