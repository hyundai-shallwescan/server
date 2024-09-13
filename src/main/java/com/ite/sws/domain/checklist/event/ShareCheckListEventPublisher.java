package com.ite.sws.domain.checklist.event;

import com.ite.sws.domain.chat.dto.ChatDTO;
import com.ite.sws.domain.checklist.dto.ShareCheckMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 공유 체크리스트 이벤트 발행기
 * @author 김민정
 * @since 2024.09.12
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.12  	김민정       최초 생성
 * 2024.09.12  	김민정       공유 체크리스트 업데이트 이벤트 발행
 * 2024.09.12  	김민정       공유 체크리스트 업데이트 채팅 이벤트 발행
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class ShareCheckListEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 공유 체크리스트 업데이트 이벤트 발행
     */
    public void publishShareCheckListEvent(ShareCheckMessageDTO messageDTO) {
        eventPublisher.publishEvent(new ShareCheckListEvent(this, messageDTO));
    }

    /**
     * 공유 체크리스트 채팅 업데이트 이벤트 발행
     */
    public void publishShareCheckListChatEvent(ChatDTO chatDTO) {
        eventPublisher.publishEvent(new ShareCheckListChatEvent(this, chatDTO));
    }
}
