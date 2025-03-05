package com.criminals.plusExponential.infrastructure.redis;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisMatchingRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String MATCHING_USERS_KEY = "matching:users";

    public void setUser(Long value) {
        redisTemplate.opsForSet().add(MATCHING_USERS_KEY, value);
    }

    public List<Long> getAllUsers() {
        Set<Object> rawSet = redisTemplate.opsForSet().members(MATCHING_USERS_KEY);
        if (rawSet == null) {
            return List.of(); // null일 경우 빈 Set 반환
        }
        return rawSet.stream()
                .map(obj -> Long.valueOf(obj.toString())) // Object를 Long으로 변환
                .collect(Collectors.toList());
    }

    public void removeUser(Long userId) {
        redisTemplate.opsForSet().remove(MATCHING_USERS_KEY, userId);
    }

    public void removeUsers(Long userId1 , Long userId2 ) {
        redisTemplate.opsForSet().remove(MATCHING_USERS_KEY, userId1,userId2);
    }

}
