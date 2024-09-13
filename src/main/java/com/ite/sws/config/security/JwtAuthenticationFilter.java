package com.ite.sws.config.security;

import com.ite.sws.exception.CustomException;
import com.ite.sws.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 인증 필터 클래스
 * @author 정은지
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25   정은지        최초 생성
 * 2024.08.29   정은지        Redis를 활용한 JWT 검증 로직 추가
 * </pre>
 */

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 요청 필터링 메서드
     * 모든 HTTP 요청에 대해 JWT 토큰을 검사하고, 유효한 경우 인증 정보를 설정
     * @param request  서블릿 요청 객체
     * @param response 서블릿 응답 객체
     * @param chain    필터 체인 객체
     * @throws IOException      입출력 예외가 발생할 경우
     * @throws ServletException 서블릿 예외가 발생할 경우
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        // 액세스 토큰 재발급은 필터에서 제외
        if (requestURI.equals("/members/reissue")) {
            logger.info("액세스토큰 재발급");
            chain.doFilter(request, response);
            return;
        }

        // Request Header에서 JWT 토큰 추출
        String token = resolveToken((HttpServletRequest) request);
        logger.info("Resolved JWT Token: " + token);

        if (token != null)
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    if (redisTemplate.hasKey("BLACKLISTED_TOKEN:" + token)) {
                        logger.info("블랙 리스트에 포함된 토큰" + token);
                        ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }

                    // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (CustomException ex) {
                logger.error("토큰 검증 중 예외 발생: " + ex.getMessage(), ex);
                ((HttpServletResponse) response).setStatus(ex.getErrorCode().getStatus());
                return;
            }
        chain.doFilter(request, response);
    }

    /**
     * Request Header에서 JWT 토큰을 추출하는 메서드
     * @param request
     * @return
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Bearer로 시작하는 JWT 토큰을 추출
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}