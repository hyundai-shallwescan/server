package com.ite.sws.domain.admin.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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



