package com.ite.sws.domain.chat.service;

import com.ite.sws.domain.chat.dto.ChatDTO;
import com.ite.sws.domain.chat.dto.GetChatRes;
import com.ite.sws.domain.chat.mapper.ChatMapper;
import com.ite.sws.domain.chat.vo.ChatMessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
 * 2024.08.26  	남진수       채팅 메시지 조회 기능 추가
 * 2024.09.03  	남진수       채팅 메시지 저장 기능 추가
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
    public void saveMessage(ChatDTO message) {
        ChatMessageVO updatedMessage = ChatMessageVO.builder()
                .cartMemberId(message.getCartMemberId())
                .cartId(message.getCartId())
                .name(message.getName())
                .payload(message.getPayload())
                .status(message.getStatus())
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
