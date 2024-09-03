package com.ite.sws.config.chat;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import java.security.Key;

/**
 * JwtUtils 클래스
 * @author 남진수
 * @since 2024.09.03
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.03  	남진수       최초 생성
 * 2024.09.03  	남진수       JWT 토큰 추출 및 유효성 검사
 * 2024.09.03  	남진수       JWT 토큰 유효성 검사
 * </pre>
 */
@Component
public class JwtUtils {

    private final Key jwtSecretKey;

    public JwtUtils(@Value("${jwt.secret.key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        jwtSecretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰 추출
     * @param accessor WebSocket 메시지 헤더
     * @return JWT 토큰
     */
    public String extractJwt(final StompHeaderAccessor accessor) {
        String bearerToken = accessor.getFirstNativeHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    /**
     * JWT 토큰 유효성 검사
     * @param token JWT 토큰
     */
    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new IllegalArgumentException("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT 토큰이 잘못되었습니다.");
        }
    }

}