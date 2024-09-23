package com.ite.sws.domain.cart.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.sws.domain.chat.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * 결제 관련 RedisSubscriber
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
public class PaymentRedisSubscriber {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public void handlePaymentMessage(String message) {
        try {
            ChatDTO paymentDTO = objectMapper.readValue(message, ChatDTO.class);
            log.info("Payment message received for Payment ID: {}", paymentDTO.getCartId());
            messagingTemplate.convertAndSend("/sub/payment/" + paymentDTO.getCartId(), paymentDTO);
        } catch (Exception e) {
            log.error("Error processing payment message", e);
        }
    }
}
