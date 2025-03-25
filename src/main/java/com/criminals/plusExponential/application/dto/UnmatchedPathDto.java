package com.criminals.plusExponential.application.dto;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.embeddable.Fare;
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

    private Fare fare;

    private int distance;

    private int duration;

    private User user;

    private int toll;



    public UnmatchedPath toEntity() {
        UnmatchedPath unmatchedPath = UnmatchedPath.builder()
                .initPoint(initPoint)
                .destinationPoint(destinationPoint)
                .fare(fare)
                .distance(distance)
                .duration(duration)
                .user(user)
                .build();

        return unmatchedPath;

    }


}
