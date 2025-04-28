package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.infrastructure.persistence.MatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.PrivateMatchedPathRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class MatchedPathServiceTest {


    private final MatchedPathService matchedPathService;
    private final MatchedPathRepository matchedPathRepository;
    private final PrivateMatchedPathRepository privateMatchedPathRepository;

    @Autowired
    public MatchedPathServiceTest(MatchedPathService matchedPathService, MatchedPathRepository matchedPathRepository, PrivateMatchedPathRepository privateMatchedPathRepository) {
        this.matchedPathService = matchedPathService;
        this.matchedPathRepository = matchedPathRepository;
        this.privateMatchedPathRepository = privateMatchedPathRepository;
    }

    @BeforeEach
    void cleanUp() {
        privateMatchedPathRepository.deleteAll();
        matchedPathRepository.deleteAll();
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

        List<MatchedPath> all = matchedPathRepository.findAll();
        Assertions.assertThat(all).hasSize(1);

        MatchedPath mp = all.get(0);

        System.out.println(mp);

    }
}
