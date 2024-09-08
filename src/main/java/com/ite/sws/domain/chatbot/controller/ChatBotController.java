package com.ite.sws.domain.chatbot.controller;

import com.ite.sws.domain.chatbot.dto.GetChatGptRes;
import com.ite.sws.domain.chatbot.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 챗봇 컨트롤러
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
@RestController
@RequiredArgsConstructor
@RequestMapping(
        value="/chatbot",
        produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
@Log4j2
public class ChatBotController {

    private final ChatBotService chatBotService;

    /**
     * 사용자 질의에 대한 챗봇 응답 반환 API
     * @param query 사용자 질의
     * @return ResponseEntity<GetChatGptRes> 챗봇 응답 객체
     */
    @GetMapping
    public ResponseEntity<GetChatGptRes> getResponse(@RequestParam String query) {
        GetChatGptRes response = chatBotService.getResponse(query);

        return ResponseEntity.ok(response);
    }
}
