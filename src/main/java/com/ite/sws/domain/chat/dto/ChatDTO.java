package com.ite.sws.domain.chat.dto;

import lombok.*;

/**
 * Chat DTO
 * @author 남진수
 * @since 2024.09.03
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.03  	남진수       최초 생성
 * </pre>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    @Setter
    private Long cartMemberId;
    private Long cartId;
    private String name;
    private String payload;
    private String status; // NORMAL, CART, CHECK
}
