package com.ite.sws.domain.checklist.event;

import com.ite.sws.domain.chat.dto.ChatDTO;
import com.ite.sws.domain.checklist.dto.ShareCheckMessageDTO;
import com.ite.sws.util.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 공유 체크리스트 이벤트 리스너 클래스
 *
 * @author 김민정
 * @since 2024.09.12
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.12  	김민정       최초 생성
 * 2024.09.12   김민정       공유체크리스트 업데이트 이벤트를 처리
 * 2024.09.12   김민정       공유체크리스트 업데이트 관련 채팅 이벤트를 처리
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class ShareCheckListEventListener {

    private final MessageSender messageSender;

    /**
     * 공유체크리스트 업데이트 이벤트를 처리하는 메서드
     * @param event 공유체크리스트 업데이트 정보를 포함한 이벤트 객체
     */
    @Async("taskExecutor")
    @EventListener
    @Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void handleShareCheckListEvent(ShareCheckListEvent event) {
        ShareCheckMessageDTO messageDTO = event.getShareCheckMessageDTO();
        messageSender.sendCartUpdateMessage(messageDTO.getCartId(), messageDTO);     // 웹소켓으로 메시지 전송
    }

    /**
     * 공유체크리스트 관련 채팅 이벤트를 처리하는 메서드
     * @param event 공유체크리스트 관련 채팅 메시지 정보를 포함한 이벤트 객체
     */
    @Async("taskExecutor")
    @EventListener
    @Retryable(value = { MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void handleShareCheckListChatEvent(ShareCheckListChatEvent event) {
        ChatDTO messageDTO = event.getChatDTO();
        messageSender.sendChatMessage(messageDTO.getCartId(), messageDTO);     // 웹소켓으로 메시지 전송
    }
}
