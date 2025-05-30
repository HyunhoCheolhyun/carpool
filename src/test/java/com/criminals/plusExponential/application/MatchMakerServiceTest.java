package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
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

            if(partnerOfA == null){
                Assertions.assertThat(partnerOfB).isEqualTo(a);
            }
            else{
                Assertions.assertThat(partnerOfA).isEqualTo(b);
            }




        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

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

            if(futureA.get() == null){
                Assertions.assertThat(futureB.get()).isEqualTo(a);
            }
            else{
                Assertions.assertThat(futureA.get()).isEqualTo(b);
            }

            if(futureC.get() == null){
                Assertions.assertThat(futureD.get()).isEqualTo(c);
            }
            else {
                Assertions.assertThat(futureC.get()).isEqualTo(d);
            }


            if(futureE.get() == null){
                Assertions.assertThat(futureF.get()).isEqualTo(e);
            }
            else {
                Assertions.assertThat(futureE.get()).isEqualTo(f);
            }



        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } catch (ExecutionException ex) {
            throw new RuntimeException(ex);
        }

    }
}