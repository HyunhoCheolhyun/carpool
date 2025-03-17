package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PrivateMatchedPath {

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
    private int fare;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int distance;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_path_id")
    private MatchedPath matchedPath;

}
