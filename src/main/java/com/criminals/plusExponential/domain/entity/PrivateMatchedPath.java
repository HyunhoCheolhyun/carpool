package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private Fare fare;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private int distance;

    @Column(nullable = false)
    private int savedAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

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
