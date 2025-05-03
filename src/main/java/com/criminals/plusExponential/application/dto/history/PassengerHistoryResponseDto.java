package com.criminals.plusExponential.application.dto.history;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class PassengerHistoryResponseDto{


    private final LocalDate date;
    private final int price;
    private final int savedAmount;
    private final int duration;
    private final int distance;


    public PassengerHistoryResponseDto(LocalDateTime dateTime, int price, int savedAmount, int duration, int distance) {
        this.date = dateTime.toLocalDate();
        this.price = price;
        this.savedAmount = savedAmount;
        this.duration = duration;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "PassengerHistoryResponseDto{" +
                "date=" + date +
                ", price=" + price +
                ", savedAmount=" + savedAmount +
                ", duration=" + duration +
                ", distance=" + distance +
                '}';
    }
}
