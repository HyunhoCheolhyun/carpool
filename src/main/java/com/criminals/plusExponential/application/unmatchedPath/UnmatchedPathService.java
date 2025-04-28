package com.criminals.plusExponential.application.unmatchedPath;

import com.criminals.plusExponential.application.MatchMakerService;
import com.criminals.plusExponential.application.PathService;
import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.common.exception.customex.ErrorCode;
import com.criminals.plusExponential.common.exception.customex.ThereIsNoUnmatchedPathException;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.kakao.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class UnmatchedPathService extends PathService {

    private final UnmatchedPathRepository unmatchedPathRepository;
    private final MatchMakerService matchMakerService;

    public UnmatchedPathService(KakaoMobilityClient km, UnmatchedPathRepository unmatchedPathRepository, UnmatchedPathRepository unmatchedPathRepository1, MatchMakerService matchMakerService) {
        super(km, unmatchedPathRepository);
        this.unmatchedPathRepository = unmatchedPathRepository1;
        this.matchMakerService = matchMakerService;
    }

    @Transactional
    public void createUnmatchedPath(UnmatchedPathDto unmatchedPathDto, CustomUserDetails customUserDetails) {

        unmatchedPathRepository.save(initFields(unmatchedPathDto, customUserDetails));
    }

    public void sendMessageToMatchMakerService(User user) throws ExecutionException, InterruptedException {

        Optional<UnmatchedPath> existingOpt = unmatchedPathRepository.findByUser(user);

        if (!existingOpt.isPresent()) throw new ThereIsNoUnmatchedPathException(ErrorCode.ThereIsNoUnmatchedPath);

        UnmatchedPath unmatchedPath = existingOpt.get();

        matchMakerService.receiveAndSendMessage(unmatchedPath.toDto());
    }



}
