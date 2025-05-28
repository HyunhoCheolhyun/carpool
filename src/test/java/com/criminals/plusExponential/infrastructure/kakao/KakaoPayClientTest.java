package com.criminals.plusExponential.infrastructure.kakao;

import com.criminals.plusExponential.application.DriverService;
import com.criminals.plusExponential.application.dto.kakao.PaymentResponseDto;
import com.criminals.plusExponential.infrastructure.redis.RedisPgTokenRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.*;

@SpringBootTest
class KakaoPayClientTest {

    @Autowired
    private DriverService driverService;

    @Autowired
    private KakaoPayClient kakaoPayClient;

    @Autowired
    private RedisPgTokenRepository redisPgTokenRepository;

//    @AfterEach
//    void cleanUp(){
//        redisPgTokenRepository.deletePgToken(1L);
//    }



    @Test
    void 결제URL요청(){
        PaymentResponseDto paymentResponseDto = kakaoPayClient.getPayment(1000,72L);
        System.out.println(paymentResponseDto.toString());
    }

//    @Test
//    void 결제승인() throws InterruptedException, ExecutionException, TimeoutException {
//        // 결제 URL 요청
//        PaymentResponseDto paymentResponseDto = kakaoPayClient.getPayment(1000);
//        System.out.println(paymentResponseDto.getNext_redirect_pc_url());
//
//        // 결제 완료될까지 폴링
//        String pgToken = null;
//        long startTime = System.currentTimeMillis();
//        long timeout = 30000; // 30초 타임아웃
//
//        while (System.currentTimeMillis() - startTime < timeout) {
//            pgToken = redisPgTokenRepository.getPgToken(1L);
//            if (pgToken != null) {
//                break;
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                throw new CompletionException(e);
//            }
//        }
//
//        if(pgToken == null){
//            throw new InterruptedException();
//        }
//
//        // pgToken을 얻은 후 결제 승인 진행
//        Boolean result = kakaoPayClient.getApprove(paymentResponseDto.getTid(), pgToken);
//        org.assertj.core.api.Assertions.assertThat(result).isEqualTo(true);
//    }
}



