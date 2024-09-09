package com.ite.sws.domain.cart.event;

import com.ite.sws.domain.cart.dto.CartItemChatDTO;
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
 * 2024.09.08  	김민정       ChatDTO 생성
 * 2024.09.08  	김민정       JSON to String 변환 메서드
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
    public void publishCartUpdateChatEvent(CartItemChatDTO cartItemChatDTO) {
        ChatDTO chatDTO = createChatDTO(cartItemChatDTO);
        eventPublisher.publishEvent(new CartUpdateChatEvent(this, chatDTO));
    }

    /**
     * ChatDTO 생성
     */
    private ChatDTO createChatDTO(CartItemChatDTO cartItemChatDTO) {
        return ChatDTO.builder()
                .cartMemberId(cartItemChatDTO.getCartMemberId())
                .cartId(cartItemChatDTO.getCartId())
                .name(cartItemChatDTO.getCartMemberName())
                .payload(toJsonPayload(cartItemChatDTO)) // JSON 변환 메서드 활용 가능
                .status("CART")
                .build();
    }

    /**
     * JSON to String 변환 메서드
     * @param cartItemChatDTO 메시지 객체
     * @return
     */
    private String toJsonPayload(CartItemChatDTO cartItemChatDTO) {
        return "{ \"action\": \"" + cartItemChatDTO.getAction() + "\", "
                + "\"productName\": \"" + cartItemChatDTO.getProductName() + "\", "
                + "\"productThumbnail\": \"" + cartItemChatDTO.getProductThumbnail() + "\", "
                + "\"quantity\": " + cartItemChatDTO.getQuantity() + " }";
    }
}
