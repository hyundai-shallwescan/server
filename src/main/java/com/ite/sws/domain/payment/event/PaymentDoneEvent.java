package com.ite.sws.domain.payment.event;

import com.ite.sws.domain.payment.dto.PaymentDoneDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 결제 완료 이벤트 클래스
 *
 * @author 김민정
 * @since 2024.09.11
 * @version 1.0
 *
 * <pre>
 * 수정일        수정자       수정내용
 * ----------  --------    ---------------------------
 * 2024.09.11  	김민정       최초 생성
 * </pre>
 */
@Getter
public class PaymentDoneEvent extends ApplicationEvent {

    private final PaymentDoneDTO paymentDoneDTO;

    public PaymentDoneEvent(Object source, PaymentDoneDTO paymentDoneDTO) {
        super(source);
        this.paymentDoneDTO = paymentDoneDTO;
    }
}
