package com.ite.sws.domain.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetPaymentHistoryRes {
  private Long paymentId;
  private Long userId;
  private String userName;
  private LocalDateTime createdAt;
}
