package com.ite.sws.domain.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
public class PaymentEvent {
  private Long paymentId;
  private Long userId;
  private String userName;
  private LocalDateTime createdAt;
}
