package com.ite.sws.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.sws.exception.ErrorResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ite.sws.exception.ErrorCode.REQUIRED_LOGIN;

/**
 * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 발생하는 예외 처리
 * @author 정은지
 * @since 2024.08.25
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.25   정은지        최초 생성
 * </pre>
 */

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @param authException 인증 예외 객체
     * @throws IOException I/O 오류 발생 시
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(REQUIRED_LOGIN.getStatus())
                .errorCode(REQUIRED_LOGIN.name())
                .message(REQUIRED_LOGIN.getMessage())
                .build();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}