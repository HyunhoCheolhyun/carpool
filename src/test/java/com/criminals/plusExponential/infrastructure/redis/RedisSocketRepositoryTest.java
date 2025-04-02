package com.criminals.plusExponential.infrastructure.redis;

import com.criminals.plusExponential.domain.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
//class RedisSocketRepositoryTest {
//
//    @Autowired
//    private RedisSocketRepository redisSocketRepository;
//
//    private final Long userId = 1234L;
//
//    @Test
//    void save(){
//
//        String socketId = "12345";
//
//        redisSocketRepository.setSocketId(userId, socketId, Role.PASSENGER);
//
//        String savedSocketId = redisSocketRepository.getSocketId(userId);
//
//        Assertions.assertThat(socketId).isEqualTo(savedSocketId);
//    }
//
//    @AfterEach
//    void cleanUp(){
//        redisSocketRepository.deleteSocketId(userId, Role.PASSENGER);
//    }
//
//
//}