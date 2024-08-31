package com.ite.sws.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 추천 Response DTO
 * @author 김민정
 * @since 2024.08.30
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.30  	김민정       최초 생성
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetProductRecommendationRes {
    private String message;
    private Long remainingParkingFee;  // 남은 주차 정산 금액
    private Long productId;
    private String productName;
    private Long productPrice;
    private String thumbnailImage;
}
