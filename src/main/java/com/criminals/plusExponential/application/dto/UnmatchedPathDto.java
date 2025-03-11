package com.criminals.plusExponential.application.dto;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnmatchedPathDto {

    private Coordinate init;
    private Coordinate destination;

}
