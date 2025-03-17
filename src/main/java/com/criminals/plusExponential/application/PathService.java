package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.InnerClass;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PathService {

    private final KakaoMobilityClient km;
    private final UnmatchedPathRepository unmatchedPathRepository;

    public class Summary {
        int fare;
        int distance;
        int duration;
    }


    public Summary getSummary(Coordinate point1, Coordinate point2) {
        Map<String, Object> routeData = km.getResponse(point1, point2);

        Map<String, Integer> fareMap = (Map<String, Integer>) routeData.get("fare");

        Integer fare = fareMap.get("taxi");
        Integer duration = (Integer) routeData.get("duration");
        Integer distance = (Integer) routeData.get("distance");

        Summary summary = new Summary();
        summary.fare = fare;
        summary.duration = duration;
        summary.distance = distance;

        return summary;


    }



    public Summary getSummary(Coordinate point1, Coordinate point2, Coordinate point3, Coordinate point4) {
        Map<String, Object> routeData = km.getResponse(point1, point2, point3, point4);

        Map<String, Integer> fareMap = (Map<String, Integer>) routeData.get("fare");
        Integer fare = fareMap.get("taxi");
        Integer duration = (Integer) routeData.get("duration");
        Integer distance = (Integer) routeData.get("distance");

        Summary summary = new Summary();
        summary.distance = distance;
        summary.duration = duration;
        summary.fare = fare;

        return summary;


    }





    public UnmatchedPath initFields(UnmatchedPathDto unmatchedPathDto, CustomUserDetails customUserDetails) {

        Summary summary = getSummary(unmatchedPathDto.getInitPoint(), unmatchedPathDto.getDestinationPoint());

        int taxiFare = summary.fare;
        int duration = summary.duration;
        int distance = summary.distance;

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
