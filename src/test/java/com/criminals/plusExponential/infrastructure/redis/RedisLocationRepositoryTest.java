package com.criminals.plusExponential.infrastructure.redis;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisLocationRepositoryTest {

    @Autowired
    private RedisLocationRepository redisLocationRepository;

    // 10KM 이내 기사에게만 전송
    @Test
    void sendDriverWithinDistance(){
        Coordinate matchedPathOrigin = new Coordinate(37.5665,126.9780);

        Coordinate posA = new Coordinate(37.5700,126.9800);
        Coordinate posB = new Coordinate(37.5600,126.9700);
        Coordinate posC = new Coordinate(37.7000,127.1000);
        Coordinate posD = new Coordinate(37.4500,126.9000);
        Coordinate posE = new Coordinate(37.4000,127.2000);

        redisLocationRepository.saveLocation("A", posA);
        redisLocationRepository.saveLocation("B", posB);
        redisLocationRepository.saveLocation("C", posC);
        redisLocationRepository.saveLocation("D", posD);
        redisLocationRepository.saveLocation("E", posE);

        List<String> result = redisLocationRepository.findNearbyDrivers(matchedPathOrigin, 10L);

        Assertions.assertThat(result)
                .containsExactlyInAnyOrder("A", "B") // A, B만 포함
                .doesNotContain("C", "D", "E"); // C, D, E는 미포함
    }

    @AfterEach
    void clearRedis(){
        redisLocationRepository.clearAll();
    }


}