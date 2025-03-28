package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MatchedPath extends BaseTimeEntity implements Comparable<MatchedPath> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column
    private Coordinate initPoint;

    @Embedded
    @Column
    private Coordinate destinationPoint;

    @Embedded
    @Column
    private Coordinate firstWayPoint;

    @Embedded
    @Column
    private Coordinate secondWayPoint;

    @Embedded
    @Column
    private Fare fare;


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

    public MatchedPath(int type, UnmatchedPathDto newRequest, UnmatchedPathDto partner) {
        this.type = type;

        switch (type) {

            case 0:
                this.initPoint = newRequest.getInitPoint();
                this.firstWayPoint = partner.getInitPoint();
                this.secondWayPoint = newRequest.getDestinationPoint();
                this.destinationPoint = partner.getDestinationPoint();
                break;

            case 1:
                this.initPoint = newRequest.getInitPoint();
                this.firstWayPoint = partner.getInitPoint();
                this.secondWayPoint = partner.getDestinationPoint();
                this.destinationPoint = newRequest.getDestinationPoint();
                break;

            case 2:
                this.initPoint = partner.getInitPoint();
                this.firstWayPoint = newRequest.getInitPoint();
                this.secondWayPoint = partner.getDestinationPoint();
                this.destinationPoint = newRequest.getDestinationPoint();
                break;

            case 3:
                this.initPoint = partner.getInitPoint();
                this.firstWayPoint = newRequest.getInitPoint();
                this.secondWayPoint = newRequest.getDestinationPoint();
                this.destinationPoint = partner.getDestinationPoint();
                break;

        }

    }

    @Override
    public String toString() {
        return "MatchedPath{" +
                "id=" + id +
                ", initPoint=" + initPoint +
                ", destinationPoint=" + destinationPoint +
                ", firstWayPoint=" + firstWayPoint +
                ", secondWayPoint=" + secondWayPoint +
                ", fare=" + fare +
                ", distance=" + distance +
                ", duration=" + duration +
                ", type=" + type +
                ", privateMatchedPaths=" + privateMatchedPaths +
                '}';
    }
}
