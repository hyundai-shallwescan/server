package com.ite.sws.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private Long cartMemberId;
    private Long cartId;
    private String payload;
    private String status;
}
