package com.ite.sws.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 채팅 메시지 조회 응답 DTO
 * @author 남진수
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	남진수       최초 생성
 * </pre>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetChatRes {
    private Long chatMessageId;
    private Long cartMemberId;
    private Long cartId;
    private String payload;
    private LocalDateTime createdAt;
    private String status;
}

