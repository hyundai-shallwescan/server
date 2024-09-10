package com.ite.sws.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 내역을 조회하기 위한 DTO
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	구지웅      최초 생성
 * </pre>
 *
 */


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
