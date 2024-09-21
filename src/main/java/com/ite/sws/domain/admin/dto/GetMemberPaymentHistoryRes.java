package com.ite.sws.domain.admin.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * 멤버의 결제 내역을 받을 수 있는 DTO
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27   구지웅      최초 생성
 * </pre>
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetMemberPaymentHistoryRes {
    private Long paymentId;
    private Long totalAmountSum;
    private LocalDateTime paymentCreatedAt;
    private List<PurchasedProduct> purchasedProducts;
}

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
 class PurchasedProduct {
    private Long productId;
    private String productName;
    private String thumbnailImage;
    private Integer quantity;
    private Long price;
}



