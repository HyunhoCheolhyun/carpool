package com.criminals.plusExponential.application.unmatchedPath;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class unmatchedPathService {
    private final KakaoMobilityClient km;

    public unmatchedPathService(KakaoMobilityClient km) {
        this.km = km;
    }

    public UnmatchedPath generatePoint(UnmatchedPathDto dto) {

        return new UnmatchedPath(dto.getInit(), dto.getDestination());

    }

    public Map<String, Integer> getSummary(UnmatchedPath unmatchedPath) {
        Map<String, Integer> result = new HashMap<>();

        Map<String, Object> routeData = km.getSummary(unmatchedPath);


        Map<String, Object> summary = (Map<String, Object>) routeData.get("summary");

        Map<String, Integer> fare = (Map<String, Integer>) summary.get("fare");

        Map<String, Integer> distance = (Map<String, Integer>) summary.get("distance");
        Map<String, Integer> duration = (Map<String, Integer>) summary.get("duration");


        result.put("fare", fare.get("fare"));
        result.put("duration", duration.get("duration"));
        result.put("distance", distance.get("distance"));

        return result;
    }

    public void createUnmatchedPath(UnmatchedPath unmatchedPath, User user, Map<String, Integer> getSummaryResult) {

        int fare = getSummaryResult.get("fare");
        int distance = getSummaryResult.get("distance");
        int duration = getSummaryResult.get("duration");

        unmatchedPath.setFare(fare);
        unmatchedPath.setDuration(duration);
        unmatchedPath.setDistance(distance);


        unmatchedPath.setUser(user);

    }
}
