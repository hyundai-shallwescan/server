package com.ite.sws.domain.chat.mapper;

import com.ite.sws.domain.chat.dto.GetChatRes;
import com.ite.sws.domain.chat.vo.ChatMessageVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 채팅 매퍼 인터페이스
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
@Mapper
public interface ChatMapper {

    /**
     * 채팅 메시지 저장
     * @param message 채팅 메시지
     */
    void insertMessage(ChatMessageVO message);

    /**
     * 채팅 메시지 조회
     * @param cartId 장바구니 식별자
     * @return 채팅 메시지 리스트
     */
    List<GetChatRes> selectMessagesByCartId(Long cartId);
}
