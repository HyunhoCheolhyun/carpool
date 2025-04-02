package com.criminals.plusExponential.application;
import com.criminals.plusExponential.application.dto.kakao.PaymentResponseDto;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.kakao.KakaoPayClient;
import com.criminals.plusExponential.infrastructure.persistence.MatchedPathRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final KakaoPayClient kakaoPayClient;
    private final Set<Long> operatingSet = new HashSet<>();
    private final Map<Long,String> tidMap= new HashMap<>();

    public void accept(Long matchedPathId){
        // 첫번째 배차만 매칭
        if(operatingSet.contains(matchedPathId)){
            throw new DataIntegrityViolationException("이미 배차가 완료되었습니다.");
        };
        operatingSet.add(matchedPathId);

        // USER와 MATCHEDPATh 정보 조회
        MatchedPath matchedPath = matchedPathRepository.findUsersById(matchedPathId)
                .orElseThrow(()-> new EntityNotFoundException("MatchedPath가 유효하지 않습니다: "+matchedPathId));
        User userA = matchedPath.getPrivateMatchedPaths().get(0).getUser();
        User userB = matchedPath.getPrivateMatchedPaths().get(1).getUser();
        PrivateMatchedPath matchedPathA = matchedPath.getPrivateMatchedPaths().get(0);
        PrivateMatchedPath matchedPathB = matchedPath.getPrivateMatchedPaths().get(0);

        // 결제 URL 요청, TID 저장
        PaymentResponseDto paymentResponseA = kakaoPayClient.getPayment(matchedPathA.getFare().getTotal());
        PaymentResponseDto paymentResponseB = kakaoPayClient.getPayment(matchedPathB.getFare().getTotal());
        tidMap.put(userA.getId(),paymentResponseA.getTid());
        tidMap.put(userB.getId(),paymentResponseB.getTid());

        //TODO 결제완료 됐는지 폴링
    }
}
