package com.criminals.plusExponential.infrastructure.config;
import com.criminals.plusExponential.common.interceptor.RedisHandshakeInterceptor;
import com.criminals.plusExponential.infrastructure.redis.RedisMatchingRepository;
import com.criminals.plusExponential.infrastructure.redis.RedisSocketRepository;
import com.criminals.plusExponential.infrastructure.socket.CustomWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final RedisSocketRepository redisSocketRepository;
    private final RedisMatchingRepository redisMatchingRepository;

    @Bean
    public WebSocketHandler customWebSocketHandler() {
        return new CustomWebSocketHandler(redisSocketRepository,redisMatchingRepository);
    }

    @Bean
    public RedisHandshakeInterceptor redisHandshakeInterceptor() {
        return new RedisHandshakeInterceptor();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customWebSocketHandler(), "/ws-stomp")
                .addInterceptors(redisHandshakeInterceptor())
                .withSockJS();
    }
}