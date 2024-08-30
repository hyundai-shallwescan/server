package com.ite.sws.domain.chat.controller;

import com.ite.sws.domain.chat.dto.ChatDTO;
import com.ite.sws.domain.chat.dto.GetChatRes;
import com.ite.sws.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 채팅 컨트롤러
 * @author 남진수
 * @since 2024.08.26
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.08.26  	남진수       최초 생성
 * 2024.08.26  	남진수       채팅 메시지 전송 API 생성
 * 2024.08.26  	남진수       채팅 메시지 조회 API 생성
 * </pre>
 */
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    /**
     * 장바구니 입장 채팅 메시지 전송
     * @param message 채팅 메시지
     */
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatDTO message){
        System.out.println(message.getPayload());
        template.convertAndSend("/sub/chat/room/" + message.getCartId(), message);
    }

    /**
     * 채팅 메시지 전송
     * @param message 채팅 메시지
     */
    @MessageMapping(value = "/chat/message")
    public void message(ChatDTO message){
        System.out.println(message.getPayload());
        template.convertAndSend("/sub/chat/room/" + message.getCartId(), message);
    }

    /**
     * 채팅 메시지 조회
     * @param cartId 장바구니 식별자
     * @return ChatMessageVO 리스트
     */
    @GetMapping("/{cartId}/chats")
    public ResponseEntity<List<GetChatRes>> getMessages(@PathVariable Long cartId) {
        return ResponseEntity.ok(chatService.getMessages(cartId));
    }
}
