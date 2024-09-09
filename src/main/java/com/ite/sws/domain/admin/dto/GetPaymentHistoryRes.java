package com.ite.sws.domain.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저의 결제 내역을 조회하기 위한 DTO
 * @since 2024.08.26
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  	구지웅      최초 생성
 * </pre>
 *
 */



@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetPaymentHistoryRes {
  private Long paymentId;
  private Long userId;
  private String userName;
  private LocalDateTime createdAt;
}
