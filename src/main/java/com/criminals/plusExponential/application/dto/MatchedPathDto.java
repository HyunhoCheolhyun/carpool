package com.criminals.plusExponential.application.dto;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchedPathDto {
    private Long id;
    private Coordinate initPoint;
    private Coordinate destinationPoint;
    private Coordinate firstWayPoint;
    private Coordinate secondWayPoint;
    private Fare fare;
    private Integer distance;
    private Integer duration;
    private int type;

    public static MatchedPathDto from(MatchedPath entity) {
        return MatchedPathDto.builder()
                .id(entity.getId())
                .initPoint(entity.getInitPoint())
                .destinationPoint(entity.getDestinationPoint())
                .firstWayPoint(entity.getFirstWayPoint())
                .secondWayPoint(entity.getSecondWayPoint())
                .fare(entity.getFare())
                .distance(entity.getDistance())
                .duration(entity.getDuration())
                .type(entity.getType())
                .build();
    }
}
