package com.criminals.plusExponential.infrastructure.redis;

import com.criminals.plusExponential.domain.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.ArrayList;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class RedisSocketRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ROLE_KEY_PREFIX = "role:";

    /**
     * 소켓 아이디 저장
     */
    public void setSocketId(Long userId, String socketId, Role role) {
        // 소켓 아이디 저장
        redisTemplate.opsForValue().set(String.valueOf(userId), socketId);

        // ROLE집합에 추가 -> 현재 접속한 드라이버 전부 조회에 필요
        redisTemplate.opsForSet().add(ROLE_KEY_PREFIX + role, userId.toString());
    }

    /**
     * 승객/드라이버 소켓 ID 조회
     */
    public String getSocketId(Long userId) {
        return (String) redisTemplate.opsForValue().get(String.valueOf(userId));
    }

    /**
     * 모든 승객/드라이버 ID 조회
     */
    public Set<String> getUserIdsByRole(Role role) {
        return Objects.requireNonNull(redisTemplate.opsForSet().members(ROLE_KEY_PREFIX + role))
                .stream().map(Object::toString).collect(Collectors.toSet());
    }

    /**
     * 특정 역할의 모든 소켓 ID 조회
     */
    public List<String> getSocketIdsByRole(Role role) {
        Set<String> userIds = getUserIdsByRole(role);
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> socketIds = new ArrayList<>();
        for (String userId : userIds) {
            String socketId = getSocketId(Long.valueOf(userId));
            if (socketId != null) {
                socketIds.add(socketId);
            }
        }

        return socketIds;
    }

    /**
     * 사용자 소켓 ID 삭제 및 역할 집합에서 제거
     */
    public void deleteSocketId(Long userId, Role role) {
        // 사용자 ID - 소켓 ID 매핑 삭제
        redisTemplate.delete(String.valueOf(userId));

        // 역할 집합에서 사용자 ID 제거
        redisTemplate.opsForSet().remove(ROLE_KEY_PREFIX + role, userId.toString());
    }

}
