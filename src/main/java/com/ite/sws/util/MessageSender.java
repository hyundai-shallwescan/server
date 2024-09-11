package com.ite.sws.util;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * 웹소켓으로 메시지를 전달하는 클래스
 * @author 김민정
 * @since 2024.09.06
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.06  	김민정       최초 생성
 * 2024.09.06   김민정       장바구니 업데이트 메시지 전송
 * 2024.09.06   김민정       채팅 메시지 전송
 * 2024.09.11   김민정       결제 정보 전송
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class MessageSender {

    private final SimpMessagingTemplate template;

    /**
     * 장바구니 업데이트 메시지 전송
     * @param cartId 장바구니 ID
     * @param payload 전달할 메시지
     */
    public void sendCartUpdateMessage(Long cartId, Object payload) {
        String destination = "/sub/cart/" + cartId;
        this.template.convertAndSend(destination, payload);
    }

    /**
     * 채팅 메시지 전송
     * @param cartId 장바구니 ID
     * @param payload 전달할 메시지
     */
    public void sendChatMessage(Long cartId, Object payload) {
        String destination = "/sub/chat/" + cartId;
        this.template.convertAndSend(destination, payload);
    }

    /**
     * 결제 정보 전송
     * @param cartId 장바구니 ID
     * @param payload 결제 관련 정보
     */
    public void sendPaymentDone(Long cartId, Object payload) {
        String destination = "/sub/cart/" + cartId;
        this.template.convertAndSend(destination, payload);
    }
}
