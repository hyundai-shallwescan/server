package com.ite.sws.aop;

import static com.ite.sws.exception.ErrorCode.ALL_FILE_AND_INFO_SHOULD_BE_IN_REQUEST;
import static com.ite.sws.exception.ErrorCode.DATABASE_ERROR;
import static com.ite.sws.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.ite.sws.exception.ErrorCode.NULL_POINTER_EXCEPTION;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import com.ite.sws.exception.ErrorResponse;
import java.sql.SQLException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * 전역 예외 처리 클래스
 *
 * @author 김민정
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	김민정      최초 생성
 * 2024.08.25   김민정      SQL Exception 처리
 * </pre>
 */
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    /**
     * 커스텀 예외 처리
     * @param ex CustomException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        log.error("CustomException 발생: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(ex.getErrorCode().getStatus())
                .errorCode(ex.getErrorCode().name())
                .message(ex.getErrorCode().getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    /**
     * NullPointerException 처리
     * @param ex NullPointerException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        log.error("NullPointerException 발생: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(NULL_POINTER_EXCEPTION.getStatus())
                .errorCode(NULL_POINTER_EXCEPTION.name())
                .message(NULL_POINTER_EXCEPTION.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * SQL Exception 처리
     * @param ex SQLException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ErrorResponse> handleSQLException(SQLException ex) {
        log.error("SQLException 발생: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(DATABASE_ERROR.getStatus())
                .errorCode(DATABASE_ERROR.name())
                .message(DATABASE_ERROR.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 그 외의 모든 예외 처리
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unhandled Exception 발생: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(INTERNAL_SERVER_ERROR.getStatus())
            .errorCode(INTERNAL_SERVER_ERROR.name())
            .message(INTERNAL_SERVER_ERROR.getMessage())
            .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<ErrorResponse> handleS3Exception(AmazonS3Exception ex) {
        log.error("AmazonS3Exception 발생: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(INTERNAL_SERVER_ERROR.getStatus())
            .errorCode(INTERNAL_SERVER_ERROR.name())
            .message(INTERNAL_SERVER_ERROR.getMessage())
            .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissedMultiFilePart(MissingServletRequestPartException ex) {
        log.error("handleMissedMultiFilePart 발생: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(ALL_FILE_AND_INFO_SHOULD_BE_IN_REQUEST.getStatus())
            .errorCode(ALL_FILE_AND_INFO_SHOULD_BE_IN_REQUEST.name())
            .message(INTERNAL_SERVER_ERROR.getMessage())
            .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
