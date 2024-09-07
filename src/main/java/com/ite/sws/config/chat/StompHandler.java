package com.ite.sws.config.chat;

import com.ite.sws.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * WebSocket 토큰 추출 및 인증 처리 핸들러
 * @author 남진수
 * @since 2024.09.03
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.03  	남진수       최초 생성
 * 2024.09.03  	남진수       토큰을 추출 및 인증 처리
 * </pre>
 */
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {

    private final JwtUtils jwtUtils;
    private final JwtTokenProvider jwtTokenProvider;

    static {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    /**
     * WebSocket 연결 시, 토큰을 추출 및 인증
     * @param message WebSocket 메시지
     * @param channel 메시지 채널
     * @return 메시지
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 또는 SEND 메시지인 경우 인증 처리
        if (StompCommand.CONNECT == accessor.getCommand() || StompCommand.SEND == accessor.getCommand()) {
            processAuthentication(accessor);
        }
        return message;
    }

    /**
     * 토큰 추출 및 인증
     * @param accessor WebSocket 메시지 헤더
     */
    private void processAuthentication(StompHeaderAccessor accessor) {
        final String token = jwtUtils.extractJwt(accessor);
        jwtUtils.validateToken(token);

        Long cartMemberId = jwtTokenProvider.getCartMemberIdFromToken(token);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(cartMemberId, null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
