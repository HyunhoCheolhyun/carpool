package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class PathServiceTest {

    private final PathService pathService;


    @Autowired
    public PathServiceTest(@Qualifier("pathService") PathService pathService) {
        this.pathService = pathService;
    }

    @Test
    void getSummary_with_FourPoints() {
        UnmatchedPathDto a = new UnmatchedPathDto();
        UnmatchedPathDto b = new UnmatchedPathDto();

        a.setInitPoint(new Coordinate(37.3463, 126.9395)); //당동초등학교
        a.setDestinationPoint(new Coordinate(37.2094, 126.9769)); //수원대학교

        b.setInitPoint(new Coordinate(37.347, 126.9527)); //한세대학교
        b.setDestinationPoint(new Coordinate(37.2143, 126.9755)); //토마토 오피스텔2차

        PathService.Summary summary = pathService.getSummary(a.getInitPoint(), b.getInitPoint(), a.getDestinationPoint(), b.getDestinationPoint());

        pathService.getSummary(a.getInitPoint(), a.getDestinationPoint());


    }

    @Test
    @DisplayName("getSummary - 출발지, 경유지1, 도착지 3개 지점 요청 테스트")
    void getSummary_withThreePoints() {
        // given (테스트용 좌표 입력)
        Coordinate start = new Coordinate(37.3461, 126.9398); // 예: 당동초등학교
        Coordinate waypoint = new Coordinate(37.3471, 126.9528); // 예: 한세대학교
        Coordinate end = new Coordinate(37.2092, 126.9772); // 예: 수원대학교

        // when
        PathService.Summary summary = pathService.getSummary(start, waypoint, end);

        // then
        assertThat(summary).isNotNull();
        assertThat(summary.distance).isNotNull();
        assertThat(summary.duration).isNotNull();
        assertThat(summary.fare).isNotNull();

        Fare fare = summary.fare;
        assertThat(fare.getTaxi()).isNotNull();
        assertThat(fare.getToll()).isNotNull();

        // 출력 (확인용)
        System.out.println("Distance: " + summary.distance);
        System.out.println("Duration: " + summary.duration);
        System.out.println("Taxi fare: " + fare.getTaxi());
        System.out.println("Toll fare: " + fare.getToll());
    }


}
