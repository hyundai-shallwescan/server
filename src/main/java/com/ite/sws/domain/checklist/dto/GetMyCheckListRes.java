package com.ite.sws.domain.checklist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 마이 체크리스트 조회 Response DTO
 * @author 정은지
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	정은지       최초 생성
 * </pre>
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMyCheckListRes {
    private Long myCheckListItemId;
    private String itemName;
    private String status;
}
