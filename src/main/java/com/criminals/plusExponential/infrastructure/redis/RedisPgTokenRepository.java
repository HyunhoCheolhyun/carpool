package com.criminals.plusExponential.infrastructure.redis;

import com.criminals.plusExponential.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisPgTokenRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private static final String ROLE_KEY_PREFIX = "PGTOKEN:";

    public void publishPaymentToken(Long userId, String pgToken) {

        RTopic topic = redissonClient.getTopic("payment-tokens");
        PgTokenMessage message = new PgTokenMessage(userId, pgToken);
        topic.publish(message);
    }

}
