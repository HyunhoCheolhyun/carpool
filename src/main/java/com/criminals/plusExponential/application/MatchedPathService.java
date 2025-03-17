package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.persistence.MatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        List<MatchedPath> candidates = new ArrayList<>();

        MatchedPath matchedPath0 = new MatchedPath();
        MatchedPath matchedPath1 = new MatchedPath();
        MatchedPath matchedPath2 = new MatchedPath();
        MatchedPath matchedPath3 = new MatchedPath();

        matchedPath0.setType(0);
        matchedPath0.setInitPoint(newRequest.getInitPoint());
        matchedPath0.setFirstWayPoint(partner.getInitPoint());
        matchedPath0.setSecondWayPoint(newRequest.getDestinationPoint());
        matchedPath0.setDestinationPoint(partner.getDestinationPoint());

        Summary summary = getSummary(matchedPath0.getInitPoint(), matchedPath0.getFirstWayPoint(), matchedPath0.getSecondWayPoint(), matchedPath0.getDestinationPoint());
        matchedPath0.setFare(summary.fare);
        matchedPath0.setDistance(summary.distance);
        matchedPath0.setDuration(summary.duration);


        matchedPath1.setType(1);
        matchedPath1.setInitPoint(newRequest.getInitPoint());
        matchedPath1.setFirstWayPoint(partner.getInitPoint());
        matchedPath1.setSecondWayPoint(partner.getDestinationPoint());
        matchedPath1.setDestinationPoint(newRequest.getDestinationPoint());

        summary = getSummary(matchedPath1.getInitPoint(), matchedPath1.getFirstWayPoint(), matchedPath1.getSecondWayPoint(), matchedPath1.getDestinationPoint());
        matchedPath1.setFare(summary.fare);
        matchedPath1.setDistance(summary.distance);
        matchedPath1.setDuration(summary.duration);


        matchedPath2.setType(2);
        matchedPath2.setInitPoint(partner.getInitPoint());
        matchedPath2.setFirstWayPoint(newRequest.getInitPoint());
        matchedPath2.setSecondWayPoint(partner.getDestinationPoint());
        matchedPath2.setDestinationPoint(newRequest.getDestinationPoint());

        summary = getSummary(matchedPath2.getInitPoint(), matchedPath2.getFirstWayPoint(), matchedPath2.getSecondWayPoint(), matchedPath2.getDestinationPoint());
        matchedPath2.setFare(summary.fare);
        matchedPath2.setDistance(summary.distance);
        matchedPath2.setDuration(summary.duration);

        matchedPath3.setType(3);
        matchedPath3.setInitPoint(partner.getInitPoint());
        matchedPath3.setFirstWayPoint(newRequest.getInitPoint());
        matchedPath3.setSecondWayPoint(newRequest.getDestinationPoint());
        matchedPath3.setDestinationPoint(partner.getDestinationPoint());

        summary = getSummary(matchedPath3.getInitPoint(), matchedPath3.getFirstWayPoint(), matchedPath3.getSecondWayPoint(), matchedPath3.getDestinationPoint());
        matchedPath3.setFare(summary.fare);
        matchedPath3.setDistance(summary.distance);
        matchedPath3.setDuration(summary.duration);

        candidates.add(matchedPath0);
        candidates.add(matchedPath1);
        candidates.add(matchedPath2);
        candidates.add(matchedPath3);

        Collections.sort(candidates);

        return candidates.get(0);

    }

    @Transactional
    public void createMatchedPath(UnmatchedPathDto newRequest, UnmatchedPathDto partner) {
        MatchedPath matchedPath = initFields(newRequest, partner);
        matchedPathRepository.save(matchedPath);
        privateMatchedPathService.createPrivateMatchedPath(newRequest, partner, matchedPath);
    }
}
