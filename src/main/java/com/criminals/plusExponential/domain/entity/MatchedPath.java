package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class MatchedPath extends BaseTimeEntity implements Comparable<MatchedPath> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Coordinate initPoint;

    @Embedded
    private Coordinate destinationPoint;

    @Embedded
    private Coordinate firstWayPoint;

    @Embedded
    private Coordinate SecondWayPoint;


    @Column
    private Integer fare;

    @Column
    private Integer distance;

    @Column
    private Integer duration;

    private int type;

    @OneToMany(mappedBy = "matchedPath")
    List<PrivateMatchedPath> privateMatchedPaths = new ArrayList<>();

    @Override
    public int compareTo(MatchedPath o) {
        return this.duration - o.duration;
    }
}
