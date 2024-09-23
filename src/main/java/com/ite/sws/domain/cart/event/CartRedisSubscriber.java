package com.ite.sws.domain.cart.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.sws.domain.cart.dto.CartItemMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * 장바구니 관련 RedisSubscriber
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
public class CartRedisSubscriber {

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public void handleCartUpdateMessage(String message) {
        try {
            CartItemMessageDTO messageDTO = objectMapper.readValue(message, CartItemMessageDTO.class);
            log.info("Cart update message received for Cart ID: {}", messageDTO.getCartId());
            messagingTemplate.convertAndSend("/sub/cart/" + messageDTO.getCartId(), messageDTO);
        } catch (Exception e) {
            log.error("Error processing cart update message", e);
        }
    }
}
