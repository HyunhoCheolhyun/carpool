package com.criminals.plusExponential.infrastructure.config;
import com.criminals.plusExponential.infrastructure.redis.RedisSocketRepository;
import com.criminals.plusExponential.infrastructure.socket.WebSocketEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker  // @EnableWebSocket 대신 이것을 사용
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {  // 하나의 인터페이스만 구현

    private final RedisSocketRepository redisSocketRepository;

    @Bean
    public WebSocketEventListener webSocketEventListener() {
        return new WebSocketEventListener(redisSocketRepository);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }


}
