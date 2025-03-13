package com.criminals.plusExponential.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchingProxy {

    private final RabbitTemplate rabbitTemplate;
    private final String MATCHING_EXCHANGE = "matching-exchange";
    private final String KEY = "key";



    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(MATCHING_EXCHANGE, KEY, message);
    }
}
