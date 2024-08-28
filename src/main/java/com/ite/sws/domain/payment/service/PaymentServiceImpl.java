package com.ite.sws.domain.payment.service;

import com.ite.sws.domain.admin.service.WebFluxAsyncPaymentInfoEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements
    PaymentService {
private final WebFluxAsyncPaymentInfoEventPublisher eventPublisher;


}
