//package com.criminals.plusExponential.application.services;
//
//import com.criminals.plusExponential.domain.MatchMaker;
//import com.criminals.plusExponential.domain.MatchingProxy;
//import com.criminals.plusExponential.domain.entity.UnmatchedPath;
//import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
//import com.criminals.plusExponential.repository.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//public class PassengerServiceImpl implements PassengerService {
//
//    private final UnmatchedPath unmatchedPath;
//    private final KakaoMobilityClient kakaoMobilityClient;
//    private final MatchingProxy matchingProxy;
//    private final UserRepository userRepository;
//
//
//    public PassengerServiceImpl(UnmatchedPath unmatchedPath, KakaoMobilityClient kakaoMobilityClient, MatchingProxy matchingProxy, UserRepository userRepository) {
//        this.unmatchedPath = unmatchedPath;
//        this.kakaoMobilityClient = kakaoMobilityClient;
//        this.matchingProxy = matchingProxy;
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public Map<String, Object> getSummary(UnmatchedPath unmatchedPath) {
//        return kakaoMobilityClient.getSummary(unmatchedPath);
//    }
//
//    // 추후 작성 예정
//    public void requestMatching(UnmatchedPath unmatchedPath) {
//        // matchingProxy.doMatch();
//    }
//}
