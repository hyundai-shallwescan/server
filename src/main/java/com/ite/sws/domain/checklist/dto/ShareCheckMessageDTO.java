package com.ite.sws.domain.checklist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 공유 체크리스트 메시지 DTO
 * @author 김민정
 * @since 2024.09.12
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.12  	김민정       최초 생성
 * </pre>
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ShareCheckMessageDTO {
    private Long cartId;
    private Long productId;
    private String productName;
    private Integer productPrice;
    private String productThumbnail;
    private String status; // "checked", "unckecked"
    @Setter
    private String action; // "create", "update", "delete"
    @Builder.Default
    private String type = "shareCheck"; // "cartUpdate", "paymentDone", "shareCheck"
}
