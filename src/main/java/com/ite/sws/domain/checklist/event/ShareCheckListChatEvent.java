package com.ite.sws.domain.checklist.event;

import com.ite.sws.domain.chat.dto.ChatDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 공유 체크리스트 업데이트 관련 채팅 이벤트 클래스
 *
 * @author 김민정
 * @since 2024.09.12
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.12  	김민정       최초 생성
 * </pre>
 */
@Getter
public class ShareCheckListChatEvent extends ApplicationEvent {

    private final ChatDTO chatDTO;

    public ShareCheckListChatEvent(Object source, ChatDTO chatDTO) {
        super(source);
        this.chatDTO = chatDTO;
    }
}
