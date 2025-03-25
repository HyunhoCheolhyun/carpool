package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.infrastructure.persistence.PrivateMatchedPathRepository;
import org.springframework.stereotype.Service;

@Service
public class PrivateMatchedPathService {

    private final PrivateMatchedPathRepository privateMatchedPathRepository;

    public PrivateMatchedPathService(PrivateMatchedPathRepository privateMatchedPathRepository) {
        this.privateMatchedPathRepository = privateMatchedPathRepository;
    }

    public void createPrivateMatchedPath(MatchedPath matchedPath, UnmatchedPathDto newRequest, UnmatchedPathDto partner) {

    }
}
