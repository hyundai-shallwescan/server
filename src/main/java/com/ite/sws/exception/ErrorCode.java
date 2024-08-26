package com.ite.sws.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드
 *
 * @author 김민정
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	김민정      최초 생성
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /* 400: Bad Request */
    FIND_FAIL_MEMBER_ID(BAD_REQUEST.value(), "존재하지 않는 멤버입니다."),
    REVIEW_FILE_TYPE_NOT_PERMITTED(BAD_REQUEST.value(), "올바르지 않은 파일 형식입니다."),


    /* 403: Forbidden */
    FORBIDDEN_ACCESS(FORBIDDEN.value(), "해당 리소스에 대한 접근이 거부되었습니다."),

    /* 401: Unauthorized */
    UNAUTHORIZED_ACCESS(UNAUTHORIZED.value(), "인증이 필요합니다."),
    FCM_TOKEN_EXPIRED(UNAUTHORIZED.value(), "FCM 토큰이 만료되었습니다."),
    MEMBER_NOT_FOUND(UNAUTHORIZED.value(), "회원 정보를 찾을 수 없습니다."),

    /* NOT FOUND*/
    REVIEW_IS_NOT_EXIST(HttpStatus.NOT_FOUND.value(), "리뷰가 존재하지 않습니다."),

    /* 409: Conflict */
    LOGIN_ID_ALREADY_EXISTS(CONFLICT.value(), "이미 존재하는 아이디입니다."),

    /* 500: Internal Server Error */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 오류가 발생했습니다."),
    NULL_POINTER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "잘못된 값(NULL)이 처리되었습니다."),
    S3_PERSIST_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR.value(), "잘못된 값(NULL)이 처리되었습니다.");


    private final int status;
    private final String message;
}
