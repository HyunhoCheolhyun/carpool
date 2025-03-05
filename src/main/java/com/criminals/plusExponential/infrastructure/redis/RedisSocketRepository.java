package com.criminals.plusExponential.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class RedisSocketRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setSocketId(Long key, String value) {
        redisTemplate.opsForValue().set(String.valueOf(key), value);
    }

    public String getSocketId(Long key) {
        return (String) redisTemplate.opsForValue().get(String.valueOf(key));
    }

    public void deleteSocketId(Long key) {
        redisTemplate.delete(String.valueOf(key));
    }
}
