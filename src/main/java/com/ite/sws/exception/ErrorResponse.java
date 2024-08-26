package com.ite.sws.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * 에러 DTO
 *
 * @author 김민정
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	김민정       최초 생성
 * 2024.08.25   정은지       에러 메세지를 JSON 문자열로 변환하는 로직 추가
 * </pre>
 */
@Getter
@Builder
public class ErrorResponse {

    private final int status;
    private final String errorCode;
    private final String message;

    /**
     * ErrorResponse 객체 생성 메서드
     * @param status    HTTP 상태 코드
     * @param errorCode 애플리케이션에서 정의한 에러 코드
     * @param message   에러 메시지
     * @return 생성된 ErrorResponse 객체
     */
     public static ErrorResponse create(int status, String errorCode, Map<String, String> message) {
        ObjectMapper objectMapper = new ObjectMapper();
        String messageAsString;
        try {
            messageAsString = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            messageAsString = "{}";
        }

        return ErrorResponse.builder()
                .status(status)
                .errorCode(errorCode)
                .message(messageAsString)
                .build();
    }
}