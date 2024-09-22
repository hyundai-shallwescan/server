package com.ite.sws.domain.cart.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.sws.domain.cart.dto.CartItemMessageDTO;
import com.ite.sws.domain.chat.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 장바구니 관련 RedisPublisher
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
@Component
@RequiredArgsConstructor
public class CartRedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;  // Jackson ObjectMapper로 JSON 변환 처리

    public void publishCartUpdateMessage(Long cartId, CartItemMessageDTO messageDTO) {
        try {
            messageDTO.setCartId(cartId);
            String message = objectMapper.writeValueAsString(messageDTO); // DTO -> JSON 직렬화
            redisTemplate.convertAndSend("cartUpdateTopic", message); // 토픽 네이밍 일치 확인
        } catch (JsonProcessingException e) {
            log.error("Failed to publish Cart Update Message", e);
        }
    }

    public void publishChatMessage(Long cartId, ChatDTO chatDTO) {
        try {
            chatDTO.setCartId(cartId);
            String message = objectMapper.writeValueAsString(chatDTO); // DTO -> JSON 직렬화
            redisTemplate.convertAndSend("chatTopic", message); // 토픽 네이밍 일치 확인
        } catch (JsonProcessingException e) {
            log.error("Failed to publish Chat Message", e);
        }
    }

    public void publishPaymentMessage(Long cartId, ChatDTO chatDTO) {
        try {
            chatDTO.setCartId(cartId);
            String message = objectMapper.writeValueAsString(chatDTO); // DTO -> JSON 직렬화
            redisTemplate.convertAndSend("paymentTopic", message); // 토픽 네이밍 일치 확인
        } catch (JsonProcessingException e) {
            log.error("Failed to publish Payment Message", e);
        }
    }

}
