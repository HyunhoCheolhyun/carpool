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

    public Map<String, Object> getSummary(Coordinate point1, Coordinate point2) {
        Map<String, Object> routeData = km.getResponse(point1, point2);

        return (Map<String, Object>) routeData.get("summary");
    }

    public int getFare(Coordinate point1, Coordinate point2) {
        Map<String, Object> summary = getSummary(point1, point2);

        Map<String, Integer> fare = (Map<String, Integer>) summary.get("fare");

        return fare.get("taxi");
    }

    public int getDuration(Coordinate point1, Coordinate point2) {
        Map<String, Object> summary = getSummary(point1, point2);

        return (Integer) summary.get("duration");
    }

    public int getDistance(Coordinate point1, Coordinate point2) {
        Map<String, Object> summary = getSummary(point1, point2);

        return (Integer) summary.get("distance");
    }

    

    public UnmatchedPath initFields(UnmatchedPathDto unmatchedPathDto, CustomUserDetails customUserDetails) {


        int taxiFare = getFare(unmatchedPathDto.getInitPoint(), unmatchedPathDto.getDestinationPoint());
        int duration = getDuration(unmatchedPathDto.getInitPoint(), unmatchedPathDto.getDestinationPoint());
        int distance = getDistance(unmatchedPathDto.getInitPoint(), unmatchedPathDto.getDestinationPoint());

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
