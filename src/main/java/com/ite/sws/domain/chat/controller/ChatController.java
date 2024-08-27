package com.ite.sws.domain.chat.controller;

import com.ite.sws.domain.chat.dto.GetChatRes;
import com.ite.sws.domain.chat.service.ChatService;
import com.ite.sws.domain.chat.vo.ChatMessageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    /**
     * 채팅 메시지 전송
     * @param message 채팅 메시지
     * @return ChatMessageVO
     */
    @MessageMapping("/{cartId}/chats")  // 클라이언트가 "/ws/pub/{cartId}/chats"로 메시지를 전송하면 이 메서드가 호출됨
    @SendTo("/sub/chat/{cartId}")  // 구독한 모든 클라이언트에게 "/sub/chat" 메시지 전송
    public ChatMessageVO sendMessage(ChatMessageVO message) {
        chatService.saveMessage(message);
        return message;
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
