package com.criminals.plusExponential.infrastructure.socket;

import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.Role;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisSocketRepository redisSocketRepository;


    public void sendMatchingCompleted(Long userId, MatchedPath matchedPath) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        String socketId = Optional.ofNullable(redisSocketRepository.getSocketId(userId))
                .orElseThrow(() -> new IllegalArgumentException("Socket Disconnected.: " + userId));

        headerAccessor.setSessionId(socketId);
        headerAccessor.setLeaveMutable(true);


        // 클라이언트에게 소켓 전송
        messagingTemplate.convertAndSendToUser(
                socketId,
                "/queue/messages",
                matchedPath,
                headerAccessor.getMessageHeaders()
        );
    }

    public void sendAllDriver(MatchedPath matchedPath,int availableTime){
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);
        List<String> driverSocketIds = redisSocketRepository.getSocketIdsByRole(Role.DRIVER);

        for(String socketId : driverSocketIds){
            headerAccessor.setSessionId(socketId);

            Map<String, Object> payload = new HashMap<>();
            payload.put("matchedPath", matchedPath);
            payload.put("availableTime", availableTime);

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
