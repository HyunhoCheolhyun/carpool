package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.MatchingProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingProxyServiceProducerImpl implements MatchingProxyService{

    private final MatchingProxy matchingProxy;

    @Override
    public void initMatching(UnmatchedPathDto dto) {

    }
}
