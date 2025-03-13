package com.criminals.plusExponential.infrastructure.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageConsumer {

    @RabbitListener(queues = "q.matching.work", containerFactory="rabbitListenerContainerFactory")
    public void receiveMessage(String message)  {
        log.info("진입:{}", message);
        try{
            testMessage(message);
        } catch (RuntimeException e) {
            /**
             * 재시도하는 경우
             * 1. 매칭중인 유저 아무도 없을때
             * 2. 매칭된 상대를 뺏겼을 때
             * 재시도 안하는 경우 ( 재시도 하고싶지 않다면 AmqpRejectAndDontRequeueException(e); )
             * 1. 단순 런타임 에러
             * 등등
             */
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
