package com.criminals.plusExponential.infrastructure.socket;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.redis.RedisMatchingRepository;
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
    private final RedisMatchingRepository redisMatchingRepository;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 인터셉터에서 저장한 userId 꺼내기
        Map<String, Object> attributes = session.getAttributes();
        if (attributes.containsKey("user")) {
            User user = (User) attributes.get("user");
            log.info("[CONNECTED] userId: {}, socketId: {}", user.getId(), session.getId());
            redisSocketRepository.setSocketId(user.getId(), session.getId(), user.getRole());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Map<String, Object> attributes = session.getAttributes();
        if (attributes.containsKey("user")) {
            User user = (User) attributes.get("user");
            log.info("[DISCONNECT] userId: {}", user.getId());
            redisSocketRepository.deleteSocketId(user.getId(), user.getRole());
            redisMatchingRepository.removeUser(user.getId());
        }
    }
}
