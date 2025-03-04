package com.criminals.plusExponential.domain.embeddable;

import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Component
public class UnmatchedPath {


    @Embedded
    private Coordinate initPoint;

    @Embedded
    private Coordinate destinationPoint;

    @Column
    private int fare;

    @Column
    private long distance;

    @Column
    private long duration;


    public UnmatchedPath(Coordinate initPoint, Coordinate destinationPoint) {
        this.initPoint = initPoint;
        this.destinationPoint = destinationPoint;
    }
}
