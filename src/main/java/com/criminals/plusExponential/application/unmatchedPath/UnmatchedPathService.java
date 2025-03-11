package com.criminals.plusExponential.application.unmatchedPath;

import com.criminals.plusExponential.application.PathService;
import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import org.springframework.stereotype.Service;

@Service
public class UnmatchedPathService extends PathService {

    private final UnmatchedPathRepository unmatchedPathRepository;


    public UnmatchedPathService(KakaoMobilityClient km, UnmatchedPathRepository unmatchedPathRepository) {
        super(km);
        this.unmatchedPathRepository = unmatchedPathRepository;
    }

    public void createUnmatchedPath(UnmatchedPathDto unmatchedPathDto) {

        initFields(unmatchedPathDto);
        UnmatchedPath unmatchedPath = unmatchedPathDto.toEntity();

        unmatchedPathRepository.save(unmatchedPath);
    }
}
