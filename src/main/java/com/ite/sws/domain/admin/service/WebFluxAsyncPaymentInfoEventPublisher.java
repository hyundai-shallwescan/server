package com.ite.sws.domain.admin.service;

import com.ite.sws.domain.admin.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * 결제 event publisher
 *
 * @author 구지웅
 * @version 1.0
 * <pre>
 * 수정일        수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.28   구지웅      최초 생성
 * @since 2024.08.26
 **/
@RequiredArgsConstructor
@Component
public class WebFluxAsyncPaymentInfoEventPublisher {

 private final Sinks.Many<PaymentEvent> paymentSink = Sinks.many().multicast()
     .onBackpressureBuffer();

 public Flux<PaymentEvent> retrievePaymentEvents() {
  return paymentSink.asFlux();
 }

 public void emitPaymentEvent(PaymentEvent event) {
  paymentSink.tryEmitNext(event);
 }
}
