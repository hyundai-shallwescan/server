package com.ite.sws.domain.cart.event;

import com.ite.sws.domain.cart.dto.CartItemMessageDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 장바구니 업데이트 이벤트 클래스
 * : 장바구니 아이템이 추가되거나 수정될 때 발생하는 이벤트를 정의
 *
 * @author 김민정
 * @since 2024.09.07
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.07  	김민정       최초 생성
 * </pre>
 */
@Getter
public class CartUpdateEvent extends ApplicationEvent {
    private final CartItemMessageDTO cartItemMessageDTO;

    public CartUpdateEvent(Object source, CartItemMessageDTO cartItemMessageDTO) {
        super(source);
        this.cartItemMessageDTO = cartItemMessageDTO;
    }
}
