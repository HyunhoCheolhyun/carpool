package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull //이 클래스에서 해당 애너테이션은 matchedPath create 이후 privateMatchedPath 에 mathchedPath를 인자로 넘겨줄때 @Valid를 통해 해당 필드가 null이 아닌지 체크하기 위함
    @Embedded
    @Column(nullable = false)
    private Coordinate initPoint;

    @NotNull
    @Embedded
    @Column(nullable = false)
    private Coordinate destinationPoint;

    @NotNull
    @Embedded
    @Column(nullable = false)
    private Coordinate firstWayPoint;

    @NotNull
    @Embedded
    @Column(nullable = false)
    private Coordinate secondWayPoint;

    @NotNull
    @Embedded
    @Column(nullable = false)
    private Fare fare;

    @NotNull
    @Column(nullable = false)
    private Integer distance;

    @NotNull
    @Column(nullable = false)
    private Integer duration;

    @NotNull
    @Column(nullable = false)
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

            // A출 B출 A도 B도
            case 0:
                this.initPoint = newRequest.getInitPoint();
                this.firstWayPoint = partner.getInitPoint();
                this.secondWayPoint = newRequest.getDestinationPoint();
                this.destinationPoint = partner.getDestinationPoint();
                break;
            // A출 B출 B도 A도
            case 1:
                this.initPoint = newRequest.getInitPoint();
                this.firstWayPoint = partner.getInitPoint();
                this.secondWayPoint = partner.getDestinationPoint();
                this.destinationPoint = newRequest.getDestinationPoint();
                break;
            // B출 A출 B도 A도
            case 2:
                this.initPoint = partner.getInitPoint();
                this.firstWayPoint = newRequest.getInitPoint();
                this.secondWayPoint = partner.getDestinationPoint();
                this.destinationPoint = newRequest.getDestinationPoint();
                break;
            // B출 A출 A도 B도
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
