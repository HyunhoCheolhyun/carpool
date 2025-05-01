package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.application.privatematchedPath.PrivateMatchedPathService;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class PrivateMatchedPathServiceTest {

    private final PrivateMatchedPathService privateMatchedPathService;
    private final MatchedPathService matchedPathService;
    private final PathService pathService;

    @Autowired
    public PrivateMatchedPathServiceTest(PrivateMatchedPathService privateMatchedPathService, MatchedPathService matchedPathService, PathService pathService) {
        this.privateMatchedPathService = privateMatchedPathService;
        this.matchedPathService = matchedPathService;
        this.pathService = pathService;
    }

    @Test
    void test_setPrivateMatchedPathToll_case1_deltaPositive() {
        MatchedPath mp = new MatchedPath();
        UnmatchedPathDto newReq = new UnmatchedPathDto();
        UnmatchedPathDto partner = new UnmatchedPathDto();


        PrivateMatchedPath a = new PrivateMatchedPath();
        PrivateMatchedPath b = new PrivateMatchedPath();
        a.setFare(new Fare());
        b.setFare(new Fare());

        mp.setFare(new Fare(32800, 1200));
        newReq.setFare(new Fare(20000, 500));
        partner.setFare(new Fare(20000, 500));

        privateMatchedPathService.setPrivateMatchedPathToll(mp, newReq, partner, a, b);

        Assertions.assertThat(a.getFare().getToll()).isEqualTo(600);
        Assertions.assertThat(b.getFare().getToll()).isEqualTo(600);

    }

    @Test
    void test_setPrivateMatchedPathToll_case2_deltaNegative() {
        MatchedPath mp = new MatchedPath();
        UnmatchedPathDto newReq = new UnmatchedPathDto();
        UnmatchedPathDto partner = new UnmatchedPathDto();


        PrivateMatchedPath a = new PrivateMatchedPath();
        PrivateMatchedPath b = new PrivateMatchedPath();
        a.setFare(new Fare());
        b.setFare(new Fare());

        mp.setFare(new Fare(32800, 800));
        newReq.setFare(new Fare(20000, 500));
        partner.setFare(new Fare(20000, 500));

        privateMatchedPathService.setPrivateMatchedPathToll(mp, newReq, partner, a, b);

        Assertions.assertThat(a.getFare().getToll()).isEqualTo(400);
        Assertions.assertThat(b.getFare().getToll()).isEqualTo(400);
    }

    @Test
    void test_setPrivateMatchedPathToll_case3_delta0() {
        MatchedPath mp = new MatchedPath();
        UnmatchedPathDto newReq = new UnmatchedPathDto();
        UnmatchedPathDto partner = new UnmatchedPathDto();


        PrivateMatchedPath a = new PrivateMatchedPath();
        PrivateMatchedPath b = new PrivateMatchedPath();
        a.setFare(new Fare());
        b.setFare(new Fare());

        mp.setFare(new Fare(32800, 1000));
        newReq.setFare(new Fare(20000, 500));
        partner.setFare(new Fare(20000, 500));

        privateMatchedPathService.setPrivateMatchedPathToll(mp, newReq, partner, a, b);

        Assertions.assertThat(a.getFare().getToll()).isEqualTo(500);
        Assertions.assertThat(b.getFare().getToll()).isEqualTo(500);
    }

    @Test
    void createPrivateMatchedPath() {
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

        System.out.println(privateMatchedPaths[0]);


    }

}