package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class MatchedPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Coordinate init;

    @Embedded
    private Coordinate destination;

    @Embedded
    private Coordinate firstWay;

    @Embedded
    private Coordinate SecondWay;

    @Column
    private Integer firstFare;

    @Column
    private Integer secondFare;

    @Column
    private Integer firstDuration;

    @Column
    private Integer secondDuration;

    @Column
    private Integer totalFare;

    @Column
    private Integer totalDuration;

    @Column
    private Boolean isReal;

    @OneToMany(mappedBy = "matchedPath")
    private List<User> users;

    @OneToMany(mappedBy = "matchedPath")
    private List<UnmatchedPath> unmatchedPaths;


}
