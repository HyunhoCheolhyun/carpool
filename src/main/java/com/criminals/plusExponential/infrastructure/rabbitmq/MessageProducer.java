package com.criminals.plusExponential.infrastructure.rabbitmq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String MATCHING_EXCHANGE = "matching-exchange";
    private final String KEY = "key";



    public void sendMessage(String message) {
        log.info("큐진입: {}", message);
        rabbitTemplate.convertAndSend(MATCHING_EXCHANGE, KEY, message);
    }

}
