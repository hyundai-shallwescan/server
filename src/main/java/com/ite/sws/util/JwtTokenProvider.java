package com.ite.sws.util;

import com.ite.sws.domain.member.dto.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * JWT 토큰을 생성, 검증, 파싱하는 유틸리티 클래스
 * @author 정은지
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25   정은지        최초 생성
 * 2024.09.01   정은지        accessToken에 cartId 저장
 * </pre>
 */

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private SecretKey key;

    @Value("${jwt.secret.key}")
    private String secretKey;

//    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 5; 	// 5시간
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14; // 14일
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 14;  // 14일

    /**
     *  secretKey를 이용해 HMAC-SHA 알고리즘용 키를 초기화하는 메서드
     */
    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰 생성 메서드
     * @param authentication 인증 정보 객체
     * @param memberId 멤버 아이디
     * @return JwtToken 객체
     */
    public JwtToken generateToken(Authentication authentication, Long memberId, Long cartId) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities) // 권한 정보 추가
                .claim("memberId", memberId) // 사용자 ID 추가
                .claim("cartId", cartId) // 장바구니 ID 추가
                .setExpiration(accessTokenExpiresIn)  // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME)) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘과 키 설정
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * JWT 토큰에서 인증 정보를 조회 메서드
     * @param accessToken JWT 액세스토큰
     * @return Authentication 인증 정보 객체
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Long memberId = claims.get("memberId", Long.class);
        Long cartId = claims.get("cartId", Long.class);

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        authenticationToken.setDetails(new CustomAuthenticationDetails(memberId, cartId));  // memberId를 Details로 설정

        return authenticationToken;
    }

    /**
     * JWT 토큰의 유효성 검사 메서드
     * @param accessToken JWT 액세스토큰
     * @return boolean 토큰 유효 여부
     */
    public boolean validateToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);

            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 토큰입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.", e);
        }
        return false;
    }

    /**
     * JWT 토큰에서 Claims 객체를 파싱하는 메서드
     * @param accessToken 액세스토큰
     * @return Claims 객체
     */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 액세스 토큰의 유효시간을 반환하는 메서드
     * @param accessToken JWT 액세스토큰
     * @return
     */
    public Long getExpiration(String accessToken) {
        // 액세스 토큰 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    /**
     * JWT 토큰에서 사용자 ID를 추출하는 메서드
     * @param accessToken JWT 액세스토큰
     * @return memberId 멤버 아이디
     */
    public Long getMemberIdFromToken(String accessToken) {
        Claims claims = parseClaims(accessToken); // JWT 토큰에서 Claims를 파싱
        return claims.get("memberId", Long.class); // Claims에서 memberId를 추출하여 반환
    }

    @Getter
    @RequiredArgsConstructor
    public static class CustomAuthenticationDetails {
        private final Long memberId;
        private final Long cartId;
    }
}
