package com.ite.sws.domain.payment.event;

import com.ite.sws.domain.payment.dto.PaymentDoneDTO;
import com.ite.sws.util.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessagingException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 결제와 관련된 이벤트 리스너 클래스
 *
 * @author 김민정
 * @since 2024.09.11
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.11  	김민정       최초 생성
 * 2024.09.11   김민정       결제 완료 시, 장바구니 구독자들에게 알림
 * </pre>
 */
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final MessageSender messageSender;

    /**
     * 결제 완료 이벤트 처리
     * @param event 결제 완료 이벤트 객체
     */
    @Async("taskExecutor")
    @EventListener
    @Retryable(value = { MessagingException.class }, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public void handlePaymentDoneEvent(PaymentDoneEvent event) {
        PaymentDoneDTO messageDTO = event.getPaymentDoneDTO();
        messageSender.sendPaymentDone(messageDTO.getOldCartId(), messageDTO);     // 웹소켓으로 메시지 전송
    }
}
