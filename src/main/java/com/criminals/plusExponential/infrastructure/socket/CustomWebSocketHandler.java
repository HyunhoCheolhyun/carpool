package com.criminals.plusExponential.infrastructure.socket;
import com.criminals.plusExponential.infrastructure.redis.RedisSocketRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private final RedisSocketRepository redisSocketRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("userId: {}, socketId: {}", session.getId());
        // 인터셉터에서 저장한 userId 꺼내기
        Map<String, Object> attributes = session.getAttributes();
        if (attributes.containsKey("userId")) {
            Long userId = (Long) attributes.get("userId");
            log.info("userId: {}, socketId: {}", userId, session.getId());
            redisSocketRepository.setSocketId(userId, session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        if (attributes.containsKey("userId")) {
            Long userId = (Long) attributes.get("userId");
            redisSocketRepository.deleteSocketId(userId);
        }
    }
}
