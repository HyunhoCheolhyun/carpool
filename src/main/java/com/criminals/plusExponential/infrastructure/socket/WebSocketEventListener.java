package com.criminals.plusExponential.infrastructure.socket;

import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.redis.RedisSocketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final RedisSocketRepository redisSocketRepository;

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Authentication authentication = (Authentication) accessor.getUser();


        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails userDetails) {
                User user = userDetails.getUser();
                log.info("[CONNECTED] userId: {}, socketId: {}", user.getId(), accessor.getSessionId());
                redisSocketRepository.setSocketId(user.getId(),accessor.getSessionId(),user.getRole());
            }
        }

    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Authentication authentication = (Authentication) accessor.getUser();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails userDetails) {
                User user = userDetails.getUser();
                log.info("[DISCONNECTED] userId: {}", user.getId());
                redisSocketRepository.deleteSocketId(user.getId(),user.getRole());
            }
        }


    }
}
