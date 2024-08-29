package com.ite.sws.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회원 구매 내역 조회 Response DTO
 * @author 정은지
 * @since 2024.08.27
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27   정은지       최초 생성
 * 2024.08.29   정은지       리뷰 작성 여부 필드 추가
 * </pre>
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMemberPaymentRes {

    private Long paymentId;
    private LocalDateTime createdAt;
    private Integer amount;
    private List<GetMemberPaymentItemRes> items;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetMemberPaymentItemRes {
        private Long paymentItemId;
        private String name;
        private Integer price;
        private Integer quantity;
        private String thumbnailImage;
        private Character isReviewWritten;
    }
}
