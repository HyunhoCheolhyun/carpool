package com.criminals.plusExponential.infrastructure.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumer {

    @RabbitListener(queues = "q.matching.work")
    public void receiveMessage(String message)  {
        log.info("진입:{}", message);
        try{
            testMessage(message);
        } catch (RuntimeException e) {
            // 예외를 그냥 던지면 3번까지 재시도 됩니다.
            // 재시도 하고싶지 않다면 AmqpRejectAndDontRequeueException(e);
            throw e;
        }
    }



    private void testMessage(String message)  {
        if (message.equals("error")) {
            log.error("메세지 출력:{}", message);
            throw new RuntimeException("테스트에러");
        }
        log.info("메세지 출력:{}", message);
    }

}
