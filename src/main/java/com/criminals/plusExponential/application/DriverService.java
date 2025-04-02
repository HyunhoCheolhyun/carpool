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
import com.criminals.plusExponential.infrastructure.redis.RedisPgTokenRepository;
import com.criminals.plusExponential.infrastructure.socket.WebSocketPassengerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Slf4j
public class DriverService {
    private final MatchedPathRepository matchedPathRepository;
    private final WebSocketPassengerService webSocketPassengerService;
    private final RedisPgTokenRepository redisPgTokenRepository;
    private final KakaoPayClient kakaoPayClient;
    private final Set<Long> operatingSet = new HashSet<>();

    /**
     * 택시기사 배차수락
     * 1. 이미 배차됐는지 확인
     * 2. 카카오페이결제 URL 받아오기
     * 3. 승객들에게 URL전달
     * 4. 결제완료될때까지 폴링
     * 5. 택시기사에게 매칭완료 넘겨주기
     */
    public ResponseEntity<Void> accept(Long matchedPathId) throws InterruptedException {
        // USER와 MATCHEDPATH 정보 조회
        MatchedPath matchedPath = matchedPathRepository.findUsersById(matchedPathId)
                .orElseThrow(()-> new NotFoundException(ErrorCode.NotFoundException));
        User userA = matchedPath.getPrivateMatchedPaths().get(0).getUser();
        User userB = matchedPath.getPrivateMatchedPaths().get(1).getUser();
        PrivateMatchedPath matchedPathA = matchedPath.getPrivateMatchedPaths().get(0);
        PrivateMatchedPath matchedPathB = matchedPath.getPrivateMatchedPaths().get(0);

        // 첫번째 배차만 매칭
        if(operatingSet.contains(matchedPathId)){
            throw new DataIntegrityViolationException("이미 배차가 완료되었습니다.");
        };
        try {
            operatingSet.add(matchedPathId);

            // 결제 URL 요청
            PaymentResponseDto paymentResponseA = kakaoPayClient.getPayment(matchedPathA.getFare().getTotal());
            PaymentResponseDto paymentResponseB = kakaoPayClient.getPayment(matchedPathB.getFare().getTotal());

            //결제요청
            webSocketPassengerService.sendPaymentURL(userA.getId(),paymentResponseA);
            webSocketPassengerService.sendPaymentURL(userB.getId(),paymentResponseB);

            //결제 완료될때까지 폴링
            PgTokens pgTokens = polling(userA.getId(),userB.getId());

            //결제승인
            kakaoPayClient.getApprove(paymentResponseA.getTid(), pgTokens.pgTokenA());
            kakaoPayClient.getApprove(paymentResponseB.getTid(), pgTokens.pgTokenB());

            //결제완료 알림
            webSocketPassengerService.completePayment(userA.getId());
            webSocketPassengerService.completePayment(userB.getId());
        }
        catch (RuntimeException e){
            operatingSet.remove(matchedPathId);
            log.error(e.getMessage());
            throw e;
        }

        return ResponseEntity.ok().build();
    }


    public PgTokens polling(Long userIdA, Long userIdB) throws InterruptedException {
        String pgTokenA = null;
        String pgTokenB = null;
        long startTime = System.currentTimeMillis();
        long timeout = 30000; // 30초 타임아웃

        while (System.currentTimeMillis() - startTime < timeout) {
            pgTokenA = redisPgTokenRepository.getPgToken(userIdA);
            pgTokenB = redisPgTokenRepository.getPgToken(userIdB);
            if (pgTokenA != null && pgTokenB!=null) {
                break;
            }
            Thread.sleep(1000);
        }

        if(pgTokenA == null || pgTokenB == null){
            throw new PaymentTimeOutException(ErrorCode.PaymentTimeOutException);
        }

        return new PgTokens(pgTokenA,pgTokenB);
    }


}
