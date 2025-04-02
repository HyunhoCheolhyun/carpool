package com.criminals.plusExponential.infrastructure.redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;



@SpringBootTest
class RedisMatchingRepositoryTest {

//    @Autowired
//    private RedisMatchingRepository redisMatchingRepository;
//
//    private final List<Long> testUsers = Arrays.asList(1L, 2L, 3L, 4L, 5L);
//
//    @Test
//    void pushAndRemoveUsers() {
//
//        testUsers.forEach(redisMatchingRepository::setUser);
//
//
//        redisMatchingRepository.removeUsers(1L, 5L);
//
//
//        List<Long> allUsers = redisMatchingRepository.getAllUsers();
//        Assertions.assertThat(allUsers.size()).isEqualTo(3);
//        Assertions.assertThat(allUsers).containsExactlyInAnyOrder(2L, 3L, 4L);
//    }
//
//    @AfterEach
//    void cleanUp() {
//        testUsers.forEach(redisMatchingRepository::removeUser);
//    }
}