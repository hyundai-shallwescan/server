package com.ite.sws.domain.cart.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.sws.domain.chat.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * 채팅 관련 RedisSubscriber
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
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRedisSubscriber {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public void handleChatMessage(String message) {
        try {
            // 메시지를 ChatPayloadDTO로 변환 (payload는 이제 문자열로 처리됨)
            ChatDTO chatDTO = objectMapper.readValue(message, ChatDTO.class);

            log.info("Chat message received for Cart ID: {}", chatDTO.getCartId());
            messagingTemplate.convertAndSend("/sub/chat/" + chatDTO.getCartId(), chatDTO);
        } catch (Exception e) {
            log.error("Error processing chat message", e);
        }
    }
}
