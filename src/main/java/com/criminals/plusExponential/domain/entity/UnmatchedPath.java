package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UnmatchedPath extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Coordinate init;

    @Embedded
    private Coordinate destination;


    @Column
    private int fare;

    @Column
    private long distance;

    @Column
    private long duration;


    public UnmatchedPath(Coordinate initPoint, Coordinate destinationPoint) {
        this.init = initPoint;
        this.destination = destinationPoint;
    }
}
