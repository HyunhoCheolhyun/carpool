package com.criminals.plusExponential.application.services;

import com.criminals.plusExponential.domain.entity.UnmatchedPath;

import java.util.Map;

public interface PassengerService {
    public Map<String, Object> getSummary(UnmatchedPath unmatchedPath);
}
