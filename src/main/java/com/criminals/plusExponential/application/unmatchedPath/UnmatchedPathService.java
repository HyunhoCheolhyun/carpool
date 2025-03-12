package com.criminals.plusExponential.application.unmatchedPath;

import com.criminals.plusExponential.application.PathService;
import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UnmatchedPathService extends PathService {

    private final UnmatchedPathRepository unmatchedPathRepository;


    public UnmatchedPathService(KakaoMobilityClient km, UnmatchedPathRepository unmatchedPathRepository, UnmatchedPathRepository unmatchedPathRepository1) {
        super(km, unmatchedPathRepository);
        this.unmatchedPathRepository = unmatchedPathRepository1;
    }

    public void createUnmatchedPath(UnmatchedPathDto unmatchedPathDto, CustomUserDetails customUserDetails) {

        unmatchedPathRepository.save(initFields(unmatchedPathDto, customUserDetails));
    }

}
