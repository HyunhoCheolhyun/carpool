package com.criminals.plusExponential.domain.embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Fare {
    int taxi;
    int toll;
    int total;

    public Fare(int taxi, int toll) {
        this.taxi = taxi;
        this.toll = toll;
        this.total = taxi + toll;
    }
}
