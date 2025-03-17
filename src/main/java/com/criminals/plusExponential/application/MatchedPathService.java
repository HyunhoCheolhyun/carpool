package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.persistence.MatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MatchedPathService extends PathService{

    private final MatchedPathRepository matchedPathRepository;
    private final PrivateMatchedPathService privateMatchedPathService;

    public MatchedPathService(KakaoMobilityClient km, UnmatchedPathRepository unmatchedPathRepository, MatchedPathRepository matchedPathRepository, PrivateMatchedPathService privateMatchedPathService) {
        super(km, unmatchedPathRepository);
        this.matchedPathRepository = matchedPathRepository;
        this.privateMatchedPathService = privateMatchedPathService;
    }

    private MatchedPath initFields(UnmatchedPathDto newRequest, UnmatchedPathDto partner) {

        int minIndex = Integer.MAX_VALUE;
        int minDuration = Integer.MAX_VALUE;

        Coordinate point1 = new Coordinate();
        Coordinate point2 = new Coordinate();
        Coordinate point3 = new Coordinate();
        Coordinate point4 = new Coordinate();

        for (int i = 0; i < 4; i++) {

            switch (i) {
                case 0:
                    point1 = newRequest.getInitPoint();
                    point2 = partner.getInitPoint();
                    point3 = newRequest.getDestinationPoint();
                    point4 = partner.getDestinationPoint();
                    break;
                case 1:
                    point1 = newRequest.getInitPoint();
                    point2 = partner.getInitPoint();
                    point3 = partner.getDestinationPoint();
                    point4 = newRequest.getDestinationPoint();
                    break;
                case 2:
                    point1 = partner.getInitPoint();
                    point2 = newRequest.getInitPoint();
                    point3 = partner.getDestinationPoint();
                    point4 = newRequest.getDestinationPoint();
                    break;
                case 3:
                    point1 = partner.getInitPoint();
                    point2 = newRequest.getInitPoint();
                    point3 = newRequest.getDestinationPoint();
                    point4 = partner.getDestinationPoint();
                    break;
            }

            int duration = getSummary(point1, point2, point3, point4).duration;

            if (duration < minDuration) {
                minIndex = i;
                minDuration = duration;
            }
        }

        int fare = getSummary(point1, point2, point3, point4).fare;
        int distance = getSummary(point1, point2, point3, point4).distance;

        MatchedPath matchedPath = new MatchedPath();
        matchedPath.setDuration(minDuration);
        matchedPath.setDistance(distance);
        matchedPath.setFare(fare);
        matchedPath.setType(minIndex);

        return matchedPath;
    }

    @Transactional
    public void createMatchedPath(UnmatchedPathDto newRequest, UnmatchedPathDto partner) {
        MatchedPath matchedPath = initFields(newRequest, partner);
        matchedPathRepository.save(matchedPath);
        privateMatchedPathService.createPrivateMatchedPath(newRequest, partner, matchedPath);
    }
}
