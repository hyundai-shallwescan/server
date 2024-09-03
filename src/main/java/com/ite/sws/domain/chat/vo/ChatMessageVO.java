package com.ite.sws.domain.chat.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * ChatMessage VO
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
@Builder(toBuilder = true)
public class ChatMessageVO {
    private Long chatMessageId;
    private Long cartMemberId;
    private Long cartId;
    private String name;
    private String payload;
    private String status;
    private LocalDateTime createdAt;
}