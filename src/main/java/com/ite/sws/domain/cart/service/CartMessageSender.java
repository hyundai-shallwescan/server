package com.ite.sws.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartMessageSender {

    private final SimpMessagingTemplate template;

    public void sendMessage(String destination, Object payload) {
        this.template.convertAndSend(destination, payload);
    }
}
