package com.ite.sws.domain.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetSalesRes {
  private Long paymentId;
  private Long amount;
  private LocalDateTime createdAt;
  private Character gender;
  private Integer age;
}
