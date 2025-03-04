package com.criminals.plusExponential.application.services.user.passenger;

import com.criminals.plusExponential.application.services.user.UserService;
import com.criminals.plusExponential.application.services.user.passenger.PassengerService;
import com.criminals.plusExponential.domain.MatchingProxy;
import com.criminals.plusExponential.domain.embeddable.UnmatchedPath;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import com.criminals.plusExponential.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PassengerServiceImpl extends UserService implements PassengerService{

    private final UnmatchedPath unmatchedPath;
    private final KakaoMobilityClient kakaoMobilityClient;
    private final MatchingProxy matchingProxy;

    public PassengerServiceImpl(UserRepository userRepository, UnmatchedPath unmatchedPath, KakaoMobilityClient kakaoMobilityClient, MatchingProxy matchingProxy) {
        super(userRepository);
        this.unmatchedPath = unmatchedPath;
        this.kakaoMobilityClient = kakaoMobilityClient;
        this.matchingProxy = matchingProxy;
    }

    @Override
    public Map<String, Object> getSummary(UnmatchedPath unmatchedPath) {
        return kakaoMobilityClient.getSummary(unmatchedPath);
    }

    // 추후 작성 예정
    public void requestMatching(UnmatchedPath unmatchedPath) {
        // matchingProxy.doMatch();
    }
}
