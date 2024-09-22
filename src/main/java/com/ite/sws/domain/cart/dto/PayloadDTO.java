package com.ite.sws.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * payload DTO
 * @author 김민정
 * @since 2024.09.08
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.08  	김민정       최초 생성
 * </pre>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayloadDTO {
    private String action;
    private String productName;
    private String productThumbnail;
    private Integer quantity;
}
