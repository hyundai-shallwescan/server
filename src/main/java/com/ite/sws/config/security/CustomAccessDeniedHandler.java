package com.ite.sws.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ite.sws.exception.ErrorResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ite.sws.exception.ErrorCode.FORBIDDEN_ACCESS;

/**
 * 인증된 사용자가 권한이 없는 리소스에 접근하려고 할 때 발생하는 접근 거부 예외 처리
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
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 인증된 사용자가 권한이 없는 리소스에 접근할 때 호출
     * @param request HttpServletRequest 객체
     * @param response HttpServletResponse 객체
     * @param accessDeniedException AccessDeniedException 객체
     * @throws IOException 입출력 예외
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(FORBIDDEN_ACCESS.getStatus())
                .errorCode(FORBIDDEN_ACCESS.name())
                .message(FORBIDDEN_ACCESS.getMessage())
                .build();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
