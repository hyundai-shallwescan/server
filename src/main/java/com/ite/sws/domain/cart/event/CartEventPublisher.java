package com.ite.sws.domain.cart.event;

import com.ite.sws.domain.cart.dto.CartItemMessageDTO;
import com.ite.sws.domain.chat.dto.ChatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 장바구니 이벤트 발행기
 * @author 김민정
 * @since 2024.09.08
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.08  	김민정       최초 생성
 * 2024.09.08  	김민정       장바구니 업데이트 이벤트 발행
 * 2024.09.08  	김민정       장바구니 채팅 업데이트 이벤트 발행
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class CartEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 장바구니 업데이트 이벤트 발행
     */
    public void publishCartUpdateEvent(CartItemMessageDTO messageDTO) {
        eventPublisher.publishEvent(new CartUpdateEvent(this, messageDTO));
    }

    /**
     * 장바구니 채팅 업데이트 이벤트 발행
     */
    public void publishCartUpdateChatEvent(ChatDTO chatDTO) {
        eventPublisher.publishEvent(new CartUpdateChatEvent(this, chatDTO));
    }
}
