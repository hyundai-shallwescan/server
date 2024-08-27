package com.ite.sws.domain.chat.service;

import com.ite.sws.domain.chat.dto.GetChatRes;
import com.ite.sws.domain.chat.vo.ChatMessageVO;
import java.util.List;

/**
 * 채팅 서비스
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
public interface ChatService {

    /**
     * 채팅 메시지 저장
     * @param message 채팅 메시지
     */
    void saveMessage(ChatMessageVO message);

    /**
     * 채팅 메시지 조회
     * @param cartId 장바구니 식별자
     * @return 채팅 메시지 리스트
     */
    List<GetChatRes> getMessages(Long cartId);
}
