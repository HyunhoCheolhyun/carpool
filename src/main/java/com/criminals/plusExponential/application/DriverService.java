package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.kakao.PaymentResponseDto;
import com.criminals.plusExponential.application.dto.kakao.PgTokens;
import com.criminals.plusExponential.common.exception.customex.ErrorCode;
import com.criminals.plusExponential.common.exception.customex.NotFoundException;
import com.criminals.plusExponential.common.exception.customex.PaymentTimeOutException;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.kakao.KakaoPayClient;
import com.criminals.plusExponential.infrastructure.persistence.MatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.PrivateMatchedPathRepository;
import com.criminals.plusExponential.infrastructure.redis.PgTokenMessage;
import com.criminals.plusExponential.infrastructure.redis.RedisPgTokenRepository;
import com.criminals.plusExponential.infrastructure.socket.WebSocketPassengerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {
    private final MatchedPathRepository matchedPathRepository;
    private final WebSocketPassengerService webSocketPassengerService;
    private final KakaoPayClient kakaoPayClient;
    private final Set<Long> operatingSet = new HashSet<>();
    private final RedissonClient redissonClient;
    private final PrivateMatchedPathRepository privateMatchedPathRepository;

    /**
     * 택시기사 배차수락
     * 1. 이미 배차됐는지 확인
     * 2. matchedPath-driver 관계 저장
     * 3. 카카오페이결제 URL 받아오기
     * 4. 승객들에게 URL전달
     * 5. 결제완료될때까지 폴링
     * 6. 택시기사에게 매칭완료 넘겨주기
     */
    public ResponseEntity<Void> accept(Long matchedPathId, User driver) throws InterruptedException {
        // USER와 MATCHEDPATH 정보 조회
        MatchedPath matchedPath = matchedPathRepository.findUsersById(matchedPathId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NotFoundException));
        User userA = matchedPath.getPrivateMatchedPaths().get(0).getUser();
        User userB = matchedPath.getPrivateMatchedPaths().get(1).getUser();
        PrivateMatchedPath matchedPathA = matchedPath.getPrivateMatchedPaths().get(0);
        PrivateMatchedPath matchedPathB = matchedPath.getPrivateMatchedPaths().get(1);

        // 첫번째 배차만 매칭
        if (operatingSet.contains(matchedPathId)) {
            throw new DataIntegrityViolationException("이미 배차가 완료되었습니다.");
        }
        ;
        try {
            operatingSet.add(matchedPathId);

            matchedPath.setDriver(driver);
            matchedPathRepository.save(matchedPath);

            log.info("결제요청 전 {}", matchedPathA.getFare().getTotal());
            log.info("결제요청 전 {}", matchedPathB.getFare().getTotal());

            // 결제 URL 요청
            PaymentResponseDto paymentResponseA = kakaoPayClient.getPayment(matchedPathA.getFare().getTotal(), matchedPathId);
            PaymentResponseDto paymentResponseB = kakaoPayClient.getPayment(matchedPathB.getFare().getTotal(), matchedPathId);


            //결제요청
            webSocketPassengerService.sendPaymentURL(userA.getId(), paymentResponseA);
            webSocketPassengerService.sendPaymentURL(userB.getId(), paymentResponseB);

            //결제 완료될때까지 폴링
            PgTokens pgTokens = polling(userA.getId(), userB.getId());

            //결제승인
            kakaoPayClient.getApprove(paymentResponseA.getTid(), pgTokens.pgTokenA());
            kakaoPayClient.getApprove(paymentResponseB.getTid(), pgTokens.pgTokenB());

            //결제완료 알림
            webSocketPassengerService.completePayment(userA.getId());
            webSocketPassengerService.completePayment(userB.getId());
        } catch (RuntimeException e) {

            PrivateMatchedPath privateMatchedPathA = matchedPath.getPrivateMatchedPaths().get(0);
            PrivateMatchedPath privateMatchedPathB = matchedPath.getPrivateMatchedPaths().get(1);

            privateMatchedPathA.setUser(null);
            privateMatchedPathB.setUser(null);

            privateMatchedPathRepository.save(privateMatchedPathA);
            privateMatchedPathRepository.save(privateMatchedPathB);

            operatingSet.remove(matchedPathId);
            log.error(e.getMessage());
            throw e;
        }

        return ResponseEntity.ok().build();
    }


    /**
     * 두 승객 모두 결제완료 될때까지 폴링
     *
     * @param userIdA
     * @param userIdB
     * @return
     * @throws InterruptedException
     */
    public PgTokens polling(Long userIdA, Long userIdB) throws InterruptedException {
        CompletableFuture<String> futureA = new CompletableFuture<>();
        CompletableFuture<String> futureB = new CompletableFuture<>();

        // Redis Pub/Sub 설정
        RTopic topic = redissonClient.getTopic("payment-tokens");

        // 승객이 결제완료되면 PGTOKEN을 Pub 해주고 아래 Listener가 받아서 처리
        topic.addListener(PgTokenMessage.class, new MessageListener<PgTokenMessage>() {
            @Override
            public void onMessage(CharSequence channel, PgTokenMessage message) {
                log.info("onMessage 진입:{}", message.getPgToken());
                if (message.getUserId().equals(userIdA)) {
                    futureA.complete(message.getPgToken());
                } else if (message.getUserId().equals(userIdB)) {
                    futureB.complete(message.getPgToken());
                }
            }
        });

        try {
            // 30초안에 두 승객 모두 결제완료 되어야 정상처리
            String pgTokenA = futureA.get(30, TimeUnit.SECONDS);
            String pgTokenB = futureB.get(30, TimeUnit.SECONDS);

            return new PgTokens(pgTokenA, pgTokenB);
        } catch (Exception e) {
            throw new PaymentTimeOutException(ErrorCode.PaymentTimeOutException);
        } finally {
            topic.removeAllListeners();
        }
    }


}
