package com.criminals.plusExponential.application.history;

import com.criminals.plusExponential.application.MatchedPathService;
import com.criminals.plusExponential.application.PathService;
import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.application.dto.history.PassengerHistoryResponseDto;
import com.criminals.plusExponential.application.privatematchedPath.PrivateMatchedPathService;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.persistence.PrivateMatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class HistoryServiceTest {

    private final PathService pathService;
    private final MatchedPathService matchedPathService;
    private final PrivateMatchedPathService privateMatchedPathService;
    private final HistoryService historyService;
    private final PrivateMatchedPathRepository privateMatchedPathRepository;
    private final UserRepository userRepository;

    @Autowired
    public HistoryServiceTest(PathService pathService, MatchedPathService matchedPathService, PrivateMatchedPathService privateMatchedPathService, HistoryService historyService, PrivateMatchedPathRepository privateMatchedPathRepository, UserRepository userRepository) {
        this.pathService = pathService;
        this.matchedPathService = matchedPathService;
        this.privateMatchedPathService = privateMatchedPathService;
        this.historyService = historyService;
        this.privateMatchedPathRepository = privateMatchedPathRepository;
        this.userRepository = userRepository;
    }

    @Test
    void getPassengerHistory() {
        UnmatchedPathDto upA = new UnmatchedPathDto();
        UnmatchedPathDto upB = new UnmatchedPathDto();

        Coordinate aInitPoint = new Coordinate();
        Coordinate aDestPoint = new Coordinate();

        Coordinate bInitPoint = new Coordinate();
        Coordinate bDestPoint = new Coordinate();

        aInitPoint.setLat(37.3463);  //당동초등학교
        aInitPoint.setLng(126.9395); //당동초등학교

        aDestPoint.setLat(37.2094); //수원대학교
        aDestPoint.setLng(126.9769); //수원대학교

        bInitPoint.setLat(37.347); //한세대학교
        bInitPoint.setLng(126.9527); //한세대학교

        bDestPoint.setLat(37.2143); //토마토오피스텔 2차
        bDestPoint.setLng(126.9755); //토마토오피스텔 2차


        upA.setInitPoint(aInitPoint);
        upA.setDestinationPoint(aDestPoint);

        upB.setInitPoint(bInitPoint);
        upB.setDestinationPoint(bDestPoint);

        PathService.Summary summary = pathService.getSummary(aInitPoint, aDestPoint);

        upA.setFare(summary.fare);
        upA.setDistance(summary.distance);
        upA.setDuration(summary.duration);

        PathService.Summary summary1 = pathService.getSummary(bInitPoint, bDestPoint);

        upB.setFare(summary1.fare);
        upB.setDistance(summary1.distance);
        upB.setDuration(summary1.duration);


        MatchedPath matchedPath = matchedPathService.createMatchedPath(upA, upB);

        PrivateMatchedPath[] privateMatchedPaths = privateMatchedPathService.initFields(matchedPath, upA, upB);

        PrivateMatchedPath pm1 = privateMatchedPaths[0];
        PrivateMatchedPath pm2 = privateMatchedPaths[1];

        Optional<User> a = userRepository.findByEmail("test@email1.com");
        Optional<User> b = userRepository.findByEmail("test@email2.com");

        User userA = a.get();
        User userB = b.get();


        pm1.setUser(userA);
        pm2.setUser(userB);

        privateMatchedPathRepository.save(pm1);
        privateMatchedPathRepository.save(pm2);

        List<PassengerHistoryResponseDto> passengerHistory = historyService.getPassengerHistory(userA);

        System.out.println(passengerHistory);


    }
}
