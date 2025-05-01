package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PrivateMatchedPath extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column(nullable = false)
    private Coordinate initPoint;

    @Embedded
    @Column
    private Coordinate firstWayPoint;

    @Embedded
    @Column
    private Coordinate secondWayPoint;

    @Embedded
    @Column(nullable = false)
    private Coordinate destinationPoint;

    @Column(nullable = false)
    private Fare fare;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int distance;

    @Enumerated
    private PathStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_path_id")
    private MatchedPath matchedPath;

    @Override
    public String toString() {
        return "PrivateMatchedPath{" +
                "id=" + id +
                ", initPoint=" + initPoint +
                ", firstWayPoint=" + firstWayPoint +
                ", secondWayPoint=" + secondWayPoint +
                ", destinationPoint=" + destinationPoint +
                ", fare=" + fare +
                ", duration=" + duration +
                ", distance=" + distance +
                ", user=" + user +
                ", matchedPath=" + matchedPath +
                '}';
    }
}
