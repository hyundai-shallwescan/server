package com.ite.sws.domain.cart.event;

import com.ite.sws.domain.chat.dto.ChatDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 장바구니 채팅 알림 이벤트 클래스
 *
 * @author 김민정
 * @since 2024.09.18
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.18  	김민정       최초 생성
 * </pre>
 */
@Getter
public class ChatAlarmEvent extends ApplicationEvent {
    private final ChatDTO chatDTO;

    public ChatAlarmEvent(Object source, ChatDTO chatDTO) {
        super(source);
        this.chatDTO = chatDTO;
    }
}
