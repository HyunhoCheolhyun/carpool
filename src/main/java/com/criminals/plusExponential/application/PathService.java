package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PathService {

    private final KakaoMobilityClient km;
    private final UnmatchedPathRepository unmatchedPathRepository;

    

    public UnmatchedPath initFields(UnmatchedPathDto unmatchedPathDto, CustomUserDetails customUserDetails) {

        Map<String, Object> routeData = km.getResponse(unmatchedPathDto);


        Map<String, Object> summary = (Map<String, Object>) routeData.get("summary");
        Map<String, Integer> fare = (Map<String, Integer>) summary.get("fare");

        int taxiFare = fare.get("taxi");
        int duration = (Integer) summary.get("duration");
        int distance = (Integer) summary.get("distance");

        User user = customUserDetails.getUser();

        Optional<UnmatchedPath> existingOpt = unmatchedPathRepository.findByUser(user);

        if (existingOpt.isPresent()) {

            UnmatchedPath existing = existingOpt.get();

            existing.setInitPoint(unmatchedPathDto.getInitPoint());
            existing.setDestinationPoint(unmatchedPathDto.getDestinationPoint());
            existing.setFare(taxiFare);
            existing.setDuration(duration);
            existing.setDistance(distance);

            return existing;
        }

        unmatchedPathDto.setFare(taxiFare);
        unmatchedPathDto.setDistance(distance);
        unmatchedPathDto.setDuration(duration);
        unmatchedPathDto.setUser(user);

        return unmatchedPathDto.toEntity();


    }






}
