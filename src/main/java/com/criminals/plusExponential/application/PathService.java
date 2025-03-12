package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;

import java.util.HashMap;
import java.util.Map;


public class PathService {

    private final KakaoMobilityClient km;

    public PathService(KakaoMobilityClient km) {
        this.km = km;
    }

    public void initFields(UnmatchedPathDto unmatchedPathDto) {
        Map<String, Integer> result = new HashMap<>();

        Map<String, Object> routeData = km.getSummary(unmatchedPathDto);


        Map<String, Object> summary = (Map<String, Object>) routeData.get("summary");
        Map<String, Integer> fare = (Map<String, Integer>) summary.get("fare");

        int taxiFare = fare.get("taxi");
        int duration = (Integer) summary.get("duration");
        int distance = (Integer) summary.get("distance");

        unmatchedPathDto.setFare(taxiFare);
        unmatchedPathDto.setDistance(distance);
        unmatchedPathDto.setDuration(duration);

    }






}
