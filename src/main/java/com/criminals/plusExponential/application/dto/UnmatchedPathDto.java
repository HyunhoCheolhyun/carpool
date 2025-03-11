package com.criminals.plusExponential.application.dto;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnmatchedPathDto {

    private Coordinate initPoint;

    private Coordinate destinationPoint;

    private int fare;

    private long distance;

    private long duration;


    public UnmatchedPath toEntity() {
        UnmatchedPath unmatchedPath = UnmatchedPath.builder()
                .initPoint(initPoint)
                .destinationPoint(destinationPoint)
                .fare(fare)
                .distance(distance)
                .duration(duration)
                .build();

        return unmatchedPath;

    }
}
