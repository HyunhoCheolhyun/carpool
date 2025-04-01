package com.criminals.plusExponential.infrastructure.redis;

import com.criminals.plusExponential.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisPgTokenRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ROLE_KEY_PREFIX = "PGTOKEN:";

    /**
     * PGTOKEN 저장
     */
    public void setPgToken(Long userId, String pgToken) {
        redisTemplate.opsForValue().set(ROLE_KEY_PREFIX+String.valueOf(userId), pgToken);
    }

    /**
     * PGTOKEN 조회
     */
    public String getPgToken(Long userId) {
        return (String) redisTemplate.opsForValue().get(ROLE_KEY_PREFIX+String.valueOf(userId));
    }

    /**
     * PGTOKEN 삭제
     */
    public Long deletePgToken(Long userId) {
        return redisTemplate.opsForSet().remove(ROLE_KEY_PREFIX + userId);
    }

}
