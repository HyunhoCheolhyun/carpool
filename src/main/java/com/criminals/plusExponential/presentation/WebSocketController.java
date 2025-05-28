package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.infrastructure.redis.RedisLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisLocationRepository redisLocationRepository;

    // 택시기사 위치정보 받아서 Redis에 저장
    @MessageMapping("/location")
    public void handleLocation(@Payload Coordinate coordinate,
                               SimpMessageHeaderAccessor headerAccessor) {

        String socketId = headerAccessor.getSessionId();

        redisLocationRepository.saveLocation(socketId, coordinate);
    }

}
