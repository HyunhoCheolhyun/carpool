package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MatchMakerServiceTest {


    private final MatchMakerService matchMakerService;

    @Autowired
    public MatchMakerServiceTest(MatchMakerService matchMakerService) {
        this.matchMakerService = matchMakerService;
    }

    @Test
    @DisplayName("전체유저 : 2명 매칭 여부: O")
    void findPartnerTest() {

        //given
        UnmatchedPathDto a = new UnmatchedPathDto();
        UnmatchedPathDto b = new UnmatchedPathDto();

        a.setInitPoint(new Coordinate(37.3463, 126.9395)); //당동초등학교
        a.setDestinationPoint(new Coordinate(37.2094, 126.9769)); //수원대학교

        b.setInitPoint(new Coordinate(37.347, 126.9527)); //한세대학교
        b.setDestinationPoint(new Coordinate(37.2143, 126.9755)); //토마토 오피스텔2차

        //when
        Future<UnmatchedPathDto> futureA = matchMakerService.requestMatchAsync(a);
        Future<UnmatchedPathDto> futureB = matchMakerService.requestMatchAsync(b);

        //then
        try {
            UnmatchedPathDto partnerOfA = futureA.get();
            UnmatchedPathDto partnerOfB = futureB.get();

            Assertions.assertThat(partnerOfA).isEqualTo(b);
            Assertions.assertThat(partnerOfB).isEqualTo(a);

        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

    }



    @Test
    @DisplayName("waitingList에 본인 제외 아무도 없는경우")
    void findPartnerTest_무한루프O() throws ExecutionException, InterruptedException {

        //given
        UnmatchedPathDto a = new UnmatchedPathDto();
        //when
        a.setInitPoint(new Coordinate(37.3463, 126.9395)); //당동초등학교
        //then
        //무한루프를 돌아야됨
        Future<UnmatchedPathDto> futureA = matchMakerService.requestMatchAsync(a);
        System.out.println(futureA.get());
    }

    @Test
    @DisplayName("전체유저: 6명")
    void findPartnerTest3() {
        //given
        UnmatchedPathDto a = new UnmatchedPathDto();
        UnmatchedPathDto b = new UnmatchedPathDto();
        UnmatchedPathDto c = new UnmatchedPathDto();
        UnmatchedPathDto d = new UnmatchedPathDto();
        UnmatchedPathDto e = new UnmatchedPathDto();
        UnmatchedPathDto f = new UnmatchedPathDto();

        a.setInitPoint(new Coordinate(37.3463, 126.9395)); //당동초등학교
        a.setDestinationPoint(new Coordinate(37.2094, 126.9769)); //수원대학교

        b.setInitPoint(new Coordinate(37.347, 126.9527)); //한세대학교
        b.setDestinationPoint(new Coordinate(37.2143, 126.9755)); //토마토 오피스텔2차

        c.setInitPoint(new Coordinate(37.3452, 126.9374)); //당동중학교
        c.setDestinationPoint(new Coordinate(37.207, 127.0334)); //병점역

        d.setInitPoint(new Coordinate(37.342, 126.9395)); //용호고등학교
        d.setDestinationPoint(new Coordinate(37.2138, 127.0427));

        e.setInitPoint(new Coordinate(37.3581, 126.9332)); //산본역
        e.setDestinationPoint(new Coordinate(37.4986, 127.0307)); //강남역

        f.setInitPoint(new Coordinate(37.3502, 126.9259)); //수리산역
        f.setDestinationPoint(new Coordinate(37.4766, 126.9817)); //사당역
        //when

        Future<UnmatchedPathDto> futureA = matchMakerService.requestMatchAsync(a);
        Future<UnmatchedPathDto> futureB = matchMakerService.requestMatchAsync(b);
        Future<UnmatchedPathDto> futureC = matchMakerService.requestMatchAsync(c);
        Future<UnmatchedPathDto> futureD = matchMakerService.requestMatchAsync(d);
        Future<UnmatchedPathDto> futureE = matchMakerService.requestMatchAsync(e);
        Future<UnmatchedPathDto> futureF = matchMakerService.requestMatchAsync(f);

        //then

        try {
            Assertions.assertThat(futureA.get()).isEqualTo(b);
            Assertions.assertThat(futureB.get()).isEqualTo(a);


            Assertions.assertThat(futureC.get()).isEqualTo(d);
            Assertions.assertThat(futureD.get()).isEqualTo(c);


            Assertions.assertThat(futureE.get()).isEqualTo(f);
            Assertions.assertThat(futureF.get()).isEqualTo(e);




        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }

    }
}