package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.application.dto.DriverIdDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.infrastructure.redis.RedisLocationRepository;
import com.criminals.plusExponential.infrastructure.redis.RedisSocketRepository;
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
    private final RedisSocketRepository redisSocketRepository;

    // 택시기사 위치정보 받아서 Redis에 저장
    @MessageMapping("/location")
    public void handleLocation(@Payload Coordinate coordinate,
                               SimpMessageHeaderAccessor headerAccessor) {
        String socketId = headerAccessor.getSessionId();
        redisLocationRepository.saveLocation(socketId, coordinate);
    }

    // 택시기사 위치정보 Redis에서 클라이언트로 전송
    @MessageMapping("/location/request")
    public void sendLocation(@Payload DriverIdDto driverIdDto,
                             SimpMessageHeaderAccessor headerAccessor) {

        Long driverId = driverIdDto.getDriverId();
        String socketId = redisSocketRepository.getSocketId(driverId);
        System.out.println("socketId :" + socketId);
        Coordinate coordinate = redisLocationRepository.getLocation(socketId);

        System.out.println(coordinate);

        if (coordinate != null) {
            messagingTemplate.convertAndSend("/topic/location/" + driverId, coordinate);
        }
    }

}
