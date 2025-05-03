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
public class Fare {
    int taxi;
    int toll;
    int total;

    public Fare(int taxi, int toll) {
        this.taxi = taxi;
        this.toll = toll;
        this.total = taxi + toll;
    }

    public void setTaxi(int taxi) {
        this.taxi = taxi;
        this.total = this.taxi + this.toll;
    }

    public void setToll(int toll) {
        this.toll = toll;
        this.total = this.taxi + this.toll;
    }


    @Override
    public String toString() {
        return "Fare{" +
                "taxi=" + taxi +
                ", toll=" + toll +
                ", total=" + total +
                '}';
    }
}
