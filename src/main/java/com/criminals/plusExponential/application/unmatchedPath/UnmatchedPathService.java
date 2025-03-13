package com.criminals.plusExponential.application.unmatchedPath;

import com.criminals.plusExponential.application.MatchingProxyService;
import com.criminals.plusExponential.application.PathService;
import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.common.exception.customex.ErrorCode;
import com.criminals.plusExponential.common.exception.customex.ThereIsNoUnmatchedPathException;
import com.criminals.plusExponential.domain.MatchingProxy;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnmatchedPathService extends PathService {

    private final UnmatchedPathRepository unmatchedPathRepository;
    private final MatchingProxyService matchingProxyService;

    public UnmatchedPathService(KakaoMobilityClient km, UnmatchedPathRepository unmatchedPathRepository, MatchingProxyService matchingProxyService) {
        super(km, unmatchedPathRepository);
        this.unmatchedPathRepository = unmatchedPathRepository;
        this.matchingProxyService = matchingProxyService;
    }


    public void createUnmatchedPath(UnmatchedPathDto unmatchedPathDto, CustomUserDetails customUserDetails) {

        unmatchedPathRepository.save(initFields(unmatchedPathDto, customUserDetails));
    }

    public void sendMessageToMatchingProxy(User user) {

        Optional<UnmatchedPath> existingOpt = unmatchedPathRepository.findByUser(user);

        if (!existingOpt.isPresent()) throw new ThereIsNoUnmatchedPathException(ErrorCode.ThereIsNoUnmatchedPath);

        UnmatchedPath unmatchedPath = existingOpt.get();

        matchingProxyService.initMatching(unmatchedPath.toDto());
    }



}
