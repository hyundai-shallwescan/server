package com.ite.sws.domain.cart.event;

import com.ite.sws.domain.cart.dto.CartItemMessageDTO;
import com.ite.sws.domain.chat.service.ChatService;
import com.ite.sws.util.MessageSender;
import com.ite.sws.domain.chat.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 장바구니와 관련된 이벤트 리스너 클래스
 * : 장바구니 아이템 추가/수정 이벤트 및 관련 채팅 이벤트 발생 시, 이를 처리하고 웹소켓으로 메시지를 전송
 *
 * @author 김민정
 * @since 2024.09.07
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.07  	김민정       최초 생성
 * 2024.09.07   김민정       장바구니 업데이트 이벤트를 처리
 * 2024.09.07   김민정       장바구니 관련 채팅 이벤트를 처리
 * 2024.09.18   김민정       장바구니 변경 FCM 알림 이벤트 발행
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class CartEventListener {
    private final MessageSender messageSender;
    private final ChatService chatService;

    /**
     * 장바구니 업데이트 이벤트를 처리하는 메서드
     * @param event 장바구니 업데이트 정보를 포함한 이벤트 객체
     */
    @Async("taskExecutor")
    @EventListener
    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void handleCartUpdateEvent(CartUpdateEvent event) {
        CartItemMessageDTO messageDTO = event.getCartItemMessageDTO();
        messageSender.sendCartUpdateMessage(messageDTO.getCartId(), messageDTO);     // 웹소켓으로 메시지 전송
    }

    /**
     * 장바구니 관련 채팅 이벤트를 처리하는 메서드
     * @param event 장바구니 관련 채팅 메시지 정보를 포함한 이벤트 객체
     */
    @Async("taskExecutor")
    @EventListener
    @Retryable(value = { MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void handleCartUpdateChatEvent(CartUpdateChatEvent event) {
        ChatDTO messageDTO = event.getChatDTO();
        messageSender.sendChatMessage(messageDTO.getCartId(), messageDTO);     // 웹소켓으로 메시지 전송
    }

    /**
     * 장바구니 변경 FCM 알림 이벤트를 처리하는 메서드
     * @param event 장바구니 변경 FCM 알림 정보를 포함한 이벤트 객체
     * @throws IOException
     */
    @Async("taskExecutor")
    @EventListener
    @Retryable(value = { MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void handleChatAlarmEventEvent(ChatAlarmEvent event) throws IOException {
        ChatDTO messageDTO = event.getChatDTO();
        chatService.sendPushNotifications(messageDTO.getCartId(), messageDTO);      // FCM 알림 전송
    }
}
