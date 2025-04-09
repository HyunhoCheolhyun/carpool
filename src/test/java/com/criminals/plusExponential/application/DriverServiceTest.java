package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.kakao.PgTokens;
import com.criminals.plusExponential.infrastructure.redis.RedisPgTokenRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DriverServiceTest {

    @Autowired
    private DriverService driverService;

    @Autowired
    private RedisPgTokenRepository redisPgTokenRepository;


    @Test
    void PollingTest() throws InterruptedException {

        // 비동기로 메시지 발행
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                redisPgTokenRepository.publishPaymentToken(1L, "first");
                redisPgTokenRepository.publishPaymentToken(2L, "second");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // polling 호출
        PgTokens pgTokens = driverService.polling(1L, 2L);

        // 검증
        Assertions.assertThat(pgTokens.pgTokenA()).isEqualTo("first");
        Assertions.assertThat(pgTokens.pgTokenB()).isEqualTo("second");

    }



}