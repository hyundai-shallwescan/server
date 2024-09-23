package com.ite.sws.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 채팅(Payload DTO 포함) DTO
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
public class ChatPayloadDTO {
    @Setter
    private Long cartMemberId;
    @Setter
    private Long cartId;
    private String name;
    private PayloadDTO payload;
    private String status; // NORMAL, CART, CHECK
}
