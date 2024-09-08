package com.ite.sws.domain.chatbot.service;

import com.ite.sws.domain.chatbot.dto.GetChatGptRes;

/**
 * 챗봇 서비스
 * @author 정은지
 * @since 2024.09.08
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.09.08  	정은지        최초 생성
 * </pre>
 */
public interface ChatBotService {

    /**
     * 사용자 질의에 대한 챗봇 응답 반환
     * @param query 사용자 질의
     * @return GetChatGptRes 챗봇 응답 객체
     */
    GetChatGptRes getResponse(String query);
}
