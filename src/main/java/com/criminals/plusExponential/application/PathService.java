package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.kakao.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import lombok.RequiredArgsConstructor;


import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PathService {

    private final KakaoMobilityClient km;
    private final UnmatchedPathRepository unmatchedPathRepository;

    public class Summary {
        Fare fare;
        int distance;
        int duration;

        @Override
        public String toString() {
            return "Summary{" +
                    "fare=" + fare +
                    ", distance=" + distance +
                    ", duration=" + duration +
                    '}';
        }
    }


    public Summary getSummary(Coordinate point1, Coordinate point2) {
        Map<String, Object> routeData = km.getResponse(point1, point2);

        Map<String, Object> summaryData = (Map<String, Object>) routeData.get("summary");

        Map<String, Integer> fareMap = (Map<String, Integer>) summaryData.get("fare");

        Fare fare = new Fare(fareMap.get("taxi"), fareMap.get("toll"));


        int duration = (Integer) summaryData.get("duration");
        int distance = (Integer) summaryData.get("distance");
        
        Summary summary = new Summary();

        summary.fare = fare;
        summary.duration = duration;
        summary.distance = distance;




        return summary;


    }



    public Summary getSummary(Coordinate point1, Coordinate point2, Coordinate point3, Coordinate point4) {



        Map<String, Object> routeData = km.getResponse(point1, point2, point3, point4);

        Map<String, Object> summary = (Map<String, Object>) routeData.get("summary");

        Integer distance = (Integer) summary.get("distance");
        Integer duration = (Integer) summary.get("duration");
        Map<String, Integer> fare = (Map<String, Integer>) summary.get("fare");
        Integer taxi = fare.get("taxi");
        Integer toll = fare.get("toll");

        Summary retSummary = new Summary();

        retSummary.distance = distance;
        retSummary.duration = duration;
        retSummary.fare = new Fare(taxi, toll);


        return retSummary;

    }





    public UnmatchedPath initFields(UnmatchedPathDto unmatchedPathDto, CustomUserDetails customUserDetails) {

        Summary summary = getSummary(unmatchedPathDto.getInitPoint(), unmatchedPathDto.getDestinationPoint());

        Fare fare = summary.fare;
        int duration = summary.duration;
        int distance = summary.distance;

        User user = customUserDetails.getUser();

        Optional<UnmatchedPath> existingOpt = unmatchedPathRepository.findByUser(user);

        if (existingOpt.isPresent()) {

            UnmatchedPath existing = existingOpt.get();

            existing.setInitPoint(unmatchedPathDto.getInitPoint());
            existing.setDestinationPoint(unmatchedPathDto.getDestinationPoint());
            existing.setFare(fare);
            existing.setDuration(duration);
            existing.setDistance(distance);

            return existing;
        }

        unmatchedPathDto.setFare(fare);
        unmatchedPathDto.setDistance(distance);
        unmatchedPathDto.setDuration(duration);
        unmatchedPathDto.setUser(user);

        return unmatchedPathDto.toEntity();


    }






}
