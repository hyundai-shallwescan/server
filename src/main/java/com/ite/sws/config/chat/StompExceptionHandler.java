package com.ite.sws.config.chat;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Stomp 예외 처리 핸들러
 * @author 남진수
 * @since 2024.09.03
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.03  	남진수       최초 생성
 * 2024.09.03  	남진수       클라이언트 메시지 처리 오류 핸들링
 * 2024.09.03  	남진수       인증되지 않은 접근 오류 핸들링
 * 2024.09.03  	남진수       오류 메시지 생성
 * 2024.09.03  	남진수       클라이언트 메시지에 receiptId 설정
 * 2024.09.03  	남진수       예외 변환
 * 2024.09.03  	남진수       내부 오류 처리
 * </pre>
 */
@Component
public class StompExceptionHandler extends StompSubProtocolErrorHandler {

    private static final byte[] EMPTY_PAYLOAD = new byte[0];

    public StompExceptionHandler() {
        super();
    }

    /**
     * 클라이언트 메시지 처리 오류 핸들링
     * @param clientMessage 클라이언트 메시지
     * @param ex 예외
     * @return 메시지
     */
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage,
                                                              Throwable ex) {
        try {
            final Throwable exception = converterTrowException(ex);

            if (exception instanceof AuthenticationException) {
                throw new IllegalArgumentException("인증되지 않은 접근입니다.", exception);
            }
        } catch (IllegalArgumentException e) {
            return handleUnauthorizedException(clientMessage, e);
        }
        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    /**
     * 예외 변환
     * @param exception 예외
     * @return 예외
     */
    private Throwable converterTrowException(final Throwable exception) {
        if (exception instanceof MessageDeliveryException) {
            return exception.getCause();
        }
        return exception;
    }

    /**
     * 인증되지 않은 접근 오류 핸들링
     * @param clientMessage 클라이언트 메시지
     * @param ex 예외
     * @return 메시지
     */
    private Message<byte[]> handleUnauthorizedException(Message<byte[]> clientMessage, Throwable ex) {

        return prepareErrorMessage(
                clientMessage,
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.name()
        );
    }

    /**
     * 오류 메시지 생성
     * @param clientMessage 클라이언트 메시지
     * @param message 메시지
     * @param errorCode 오류 코드
     * @return 메시지
     */
    private Message<byte[]> prepareErrorMessage(final Message<byte[]> clientMessage,
                                                final String message,
                                                final String errorCode) {

        final StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage(errorCode);
        accessor.setLeaveMutable(true);
        setReceiptIdForClient(clientMessage, accessor);

        return MessageBuilder.createMessage(
                message != null ? message.getBytes(StandardCharsets.UTF_8) : EMPTY_PAYLOAD,
                accessor.getMessageHeaders()
        );
    }

    /**
     * 클라이언트 메시지에 receiptId 설정
     * @param clientMessage 클라이언트 메시지
     * @param accessor 메시지 헤더
     */
    private void setReceiptIdForClient(final Message<byte[]> clientMessage,
                                       final StompHeaderAccessor accessor) {

        if (Objects.isNull(clientMessage)) {
            return;
        }

        final StompHeaderAccessor clientHeaderAccessor = MessageHeaderAccessor.getAccessor(
                clientMessage, StompHeaderAccessor.class);

        final String receiptId =
                Objects.isNull(clientHeaderAccessor) ? null : clientHeaderAccessor.getReceipt();

        if (receiptId != null) {
            accessor.setReceiptId(receiptId);
        }
    }

    /**
     * 내부 오류 처리
     * @param errorHeaderAccessor 오류 메시지 헤더
     * @param errorPayload 오류 메시지
     * @param cause 예외
     * @param clientHeaderAccessor 클라이언트 메시지 헤더
     * @return 메시지
     */
    @Override
    protected Message<byte[]> handleInternal(StompHeaderAccessor errorHeaderAccessor,
                                             byte[] errorPayload,
                                             Throwable cause,
                                             StompHeaderAccessor clientHeaderAccessor) {
        return super.handleInternal(
                errorHeaderAccessor,
                errorPayload,
                cause,
                clientHeaderAccessor
        );
    }
}