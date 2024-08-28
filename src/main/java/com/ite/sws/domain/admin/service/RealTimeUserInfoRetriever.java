package com.ite.sws.domain.admin.service;

import com.ite.sws.domain.admin.dto.PaymentEvent;
import reactor.core.publisher.Flux;

public interface RealTimeUserInfoRetriever {
    Flux<PaymentEvent> retrievePaymentEvents();
}
