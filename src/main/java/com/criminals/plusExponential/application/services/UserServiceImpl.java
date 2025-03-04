package com.criminals.plusExponential.application.services;

import com.criminals.plusExponential.domain.embeddable.UnmatchedPath;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    private final UnmatchedPath unmatchedPath;
    private final KakaoMobilityClient kakaoMobilityClient;


    public UserServiceImpl(UnmatchedPath unmatchedPath, KakaoMobilityClient kakaoMobilityClient) {
        this.unmatchedPath = unmatchedPath;
        this.kakaoMobilityClient = kakaoMobilityClient;
    }

    @Override
    public Map<String, Object> getSummary(UnmatchedPath unmatchedPath) {
        return kakaoMobilityClient.getSummary(unmatchedPath);
    }
}
