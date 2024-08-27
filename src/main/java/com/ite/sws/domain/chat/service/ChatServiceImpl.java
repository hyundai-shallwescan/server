package com.ite.sws.domain.chat.service;

import com.ite.sws.domain.chat.dto.GetChatRes;
import com.ite.sws.domain.chat.mapper.ChatMapper;
import com.ite.sws.domain.chat.vo.ChatMessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 채팅 서비스 구현체
 * @author 남진수
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	남진수       최초 생성
 * 2024.08.26  	남진수       채팅 메시지 저장 기능 추가
 * 2024.08.26  	남진수       채팅 메시지 조회 기능 추가
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMapper chatMapper;

    /**
     * 채팅 메시지 저장
     * @param message 채팅 메시지
     */
    public void saveMessage(ChatMessageVO message) {
        ChatMessageVO updatedMessage = message.toBuilder()
                .createdAt(LocalDateTime.now())
                .build();
        chatMapper.insertMessage(updatedMessage);
    }

    /**
     * 채팅 메시지 조회
     * @param cartId 장바구니 식별자
     * @return 채팅 메시지 리스트
     */
    public List<GetChatRes> getMessages(Long cartId) {
        return chatMapper.selectMessagesByCartId(cartId);
    }
}
