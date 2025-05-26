package com.criminals.plusExponential.infrastructure.socket;

import com.criminals.plusExponential.application.dto.MatchedPathDto;
import com.criminals.plusExponential.common.exception.customex.BadRequestException;
import com.criminals.plusExponential.common.exception.customex.ErrorCode;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.infrastructure.redis.RedisLocationRepository;
import com.criminals.plusExponential.infrastructure.redis.RedisSocketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketDriverService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisLocationRepository redisLocationRepository;


    /**
     * 배차 요청
     *
     * @param matchedPath
     * @param availableTime
     */

    public void sendDriver(MatchedPath matchedPath, double availableTime) {
        List<String> driverSocketIds = redisLocationRepository.findNearbyDrivers(matchedPath.getInitPoint(),availableTime);

        if(driverSocketIds.isEmpty()){
            throw new BadRequestException(ErrorCode.AlreadyMatchedException);
        }

        for (String socketId : driverSocketIds) {
            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setLeaveMutable(true);
            headerAccessor.setSessionId(socketId);

            Map<String, Object> payload = new HashMap<>();
            payload.put("matchedPath", MatchedPathDto.from(matchedPath));

            // 택시기사들 모두에게 소켓 전송
            messagingTemplate.convertAndSendToUser(
                    socketId,
                    "/queue/driver",
                    payload,
                    headerAccessor.getMessageHeaders()
            );
        }
    }
}
