package com.ite.sws.domain.chat.controller;

import com.ite.sws.domain.chat.dto.ChatDTO;
import com.ite.sws.domain.chat.dto.GetChatRes;
import com.ite.sws.domain.chat.service.ChatService;
import com.ite.sws.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
 * 2024.08.26  	남진수       채팅 메시지 조회 API 생성
 * 2024.09.03  	남진수       채팅 메시지 전송 및 저장 API 생성
 * </pre>
 */
@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate template;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 채팅 메시지 전송 및 저장
     * @param message 채팅 메시지
     */
    @MessageMapping(value = "/chat/message")
    public void message(ChatDTO message, StompHeaderAccessor accessor) {

        String token = accessor.getFirstNativeHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            Long cartMemberId = jwtTokenProvider.getMemberIdFromToken(token);
            authentication = new UsernamePasswordAuthenticationToken(cartMemberId, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        if (authentication.isAuthenticated()) {
            Long cartMemberId = (Long) authentication.getPrincipal();
            message.setCartMemberId(cartMemberId);
            chatService.saveMessage(message);
            template.convertAndSend("/sub/chat/" + message.getCartId(), message);
        }
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
