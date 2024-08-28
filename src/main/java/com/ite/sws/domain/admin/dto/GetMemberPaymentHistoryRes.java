package com.ite.sws.domain.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetMemberPaymentHistoryRes {
  private Long memberId;
  private Long paymentId;
  private Long amount;
  private String paymentCard;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
