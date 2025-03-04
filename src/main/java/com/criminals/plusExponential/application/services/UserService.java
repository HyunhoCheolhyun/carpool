package com.criminals.plusExponential.application.services;

import com.criminals.plusExponential.domain.embeddable.UnmatchedPath;

import java.util.Map;

public interface UserService {
    public Map<String, Object> getSummary(UnmatchedPath unmatchedPath);
}
