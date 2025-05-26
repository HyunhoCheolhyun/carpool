package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.application.privatematchedPath.PrivateMatchedPathService;
import com.criminals.plusExponential.application.unmatchedPath.UnmatchedPathService;
import com.criminals.plusExponential.application.user.AuthService;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.persistence.MatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.PrivateMatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MatchedPathServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MatchedPathService matchedPathService;

    @Autowired
    private MatchedPathRepository matchedPathRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnmatchedPathRepository unmatchedPathRepository;

    @Autowired
    private PrivateMatchedPathService privateMatchedPathService;
    @Autowired
    private SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler;
    @Autowired
    private UnmatchedPathService unmatchedPathService;
    @Autowired
    private PrivateMatchedPathRepository privateMatchedPathRepository;


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

        a.setFare(new Fare(29360, 0));
        a.setDuration(1380);
        a.setDistance(22000);

        b.setInitPoint(new Coordinate(37.347, 126.9527)); //한세대학교
        b.setDestinationPoint(new Coordinate(37.2143, 126.9755)); //토마토 오피스텔2차

        b.setDuration(1260);
        b.setFare(new Fare(28890, 0));
        b.setDistance(22000);

        matchedPathService.createMatchedPath(a, b);

        List<MatchedPath> all = matchedPathRepository.findAll();
        Assertions.assertThat(all).hasSize(1);

        MatchedPath mp = all.get(0);

        System.out.println(mp);

    }


    @Test
    @Transactional
    void privateMatchedPathCreate() {
        // given
        User userA = new User();
        userA.setEmail("userA@example.com");
        userA.setPassword("password01!");
        userA.setUsername("userA");

        User userB = new User();
        userB.setEmail("userB@example.com");
        userB.setPassword("password01!");
        userB.setUsername("userB");

        userRepository.save(userA);
        userRepository.save(userB);

        UnmatchedPathDto a = new UnmatchedPathDto();
        UnmatchedPathDto b = new UnmatchedPathDto();

        a.setInitPoint(new Coordinate(37.21386591854519, 126.97555253354011)); //토마토 오피스텔
        a.setDestinationPoint(new Coordinate(37.50838483648765, 127.06134101282959)); // 삼성역

        b.setInitPoint(new Coordinate(37.21416391831323, 126.97895481702412)); // 수원대 정문
        b.setDestinationPoint(new Coordinate(37.49875173484248, 127.029821236394)); // 강남역

        unmatchedPathRepository.save(unmatchedPathService.initFields(a, userA));
        unmatchedPathRepository.save(unmatchedPathService.initFields(b, userB));


        MatchedPath matchedPath = matchedPathService.createMatchedPath(a, b);

        privateMatchedPathService.createPrivateMatchedPath(matchedPath, a, b);

        MatchedPath savedMatchedPath = matchedPathRepository.findById(matchedPath.getId()).get();

        Assertions.assertThat(savedMatchedPath.getPrivateMatchedPaths().get(0).getFare().getTotal())
                .isGreaterThan(0);

        Assertions.assertThat(savedMatchedPath.getPrivateMatchedPaths().get(1).getFare().getTotal())
                .isGreaterThan(0);
    }
}
