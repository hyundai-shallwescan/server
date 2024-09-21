package com.ite.sws.domain.admin.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 결제 Event가 발생했을 때 실시간으로 관리자에게 응답하기 위한 DTO
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
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
public class PaymentEvent {
  private Long paymentId;
  private Long userId;
  private String userName;
  private LocalDateTime createdAt;
}
