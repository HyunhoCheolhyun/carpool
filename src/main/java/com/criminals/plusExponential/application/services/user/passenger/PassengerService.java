package com.criminals.plusExponential.application.services.user.passenger;

import com.criminals.plusExponential.domain.embeddable.UnmatchedPath;

import java.util.Map;

public interface PassengerService {
    public Map<String, Object> getSummary(UnmatchedPath unmatchedPath);
}
