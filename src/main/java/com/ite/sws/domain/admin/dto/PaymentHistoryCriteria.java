package com.ite.sws.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
public class PaymentHistoryCriteria {
  private int page;
  private int size;
  private int year;
  private int month;
  private int day;
}
