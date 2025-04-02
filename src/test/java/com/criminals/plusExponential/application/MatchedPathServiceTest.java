package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
public class MatchedPathServiceTest {


    private final MatchedPathService matchedPathService;

    @Autowired
    public MatchedPathServiceTest(MatchedPathService matchedPathService) {
        this.matchedPathService = matchedPathService;
    }

    @Test
    void matchedPathCreate() {

        //given
        UnmatchedPathDto a = new UnmatchedPathDto();
        UnmatchedPathDto b = new UnmatchedPathDto();

        a.setInitPoint(new Coordinate(37.3463, 126.9395)); //당동초등학교
        a.setDestinationPoint(new Coordinate(37.2094, 126.9769)); //수원대학교

        b.setInitPoint(new Coordinate(37.347, 126.9527)); //한세대학교
        b.setDestinationPoint(new Coordinate(37.2143, 126.9755)); //토마토 오피스텔2차

        matchedPathService.createMatchedPath(a, b);

    }
}
