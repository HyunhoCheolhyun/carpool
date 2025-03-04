package com.criminals.plusExponential.application.services;

import com.criminals.plusExponential.domain.MatchMaker;
import com.criminals.plusExponential.domain.MatchingProxy;
import com.criminals.plusExponential.domain.embeddable.UnmatchedPath;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PassengerServiceImpl implements PassengerService {

    private final UnmatchedPath unmatchedPath;
    private final KakaoMobilityClient kakaoMobilityClient;
    private final MatchingProxy matchingProxy;


    public PassengerServiceImpl(UnmatchedPath unmatchedPath, KakaoMobilityClient kakaoMobilityClient, MatchingProxy matchingProxy) {
        this.unmatchedPath = unmatchedPath;
        this.kakaoMobilityClient = kakaoMobilityClient;
        this.matchingProxy = matchingProxy;
    }

    @Override
    public Map<String, Object> getSummary(UnmatchedPath unmatchedPath) {
        return kakaoMobilityClient.getSummary(unmatchedPath);
    }

    public void requestMatching(UnmatchedPath unmatchedPath) {
        // matchingProxy.doMatch();
    }
}
