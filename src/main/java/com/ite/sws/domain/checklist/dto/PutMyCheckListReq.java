package com.ite.sws.domain.checklist.dto;

import lombok.*;

/**
 * 마이 체크리스트 아이템 추가 Request DTO
 * @author 정은지
 * @since 2024.08.28
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28  	정은지       최초 생성
 * </pre>
 */

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PutMyCheckListReq {

    @Setter
    private Long memberId;

    private Long myCheckListCategoryId;
    private String item;
}
