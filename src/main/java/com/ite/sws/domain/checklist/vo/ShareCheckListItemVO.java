package com.ite.sws.domain.checklist.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공유 체크리스트 VO
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
public class ShareCheckListItemVO {
    private Long cartId;
    private Long productId;
    private String productName;
    private int productPrice;
    private String productThumbnail;
    private int rowCount;
}
