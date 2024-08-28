package com.ite.sws.domain.checklist.dto;

import com.ite.sws.domain.checklist.vo.ShareCheckListItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 공유 체크리스트 조회 Response DTO
 * @author 김민정
 * @since 2024.08.27
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.27  	김민정       최초 생성
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class GetShareCheckListRes {
    private Long cartId;
    private List<GetShareCheckRes> items;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class GetShareCheckRes {
        private Long productId;
        private String productName;
        private int productPrice;
        private String productThumbnail;
        private String status;
    }
}
