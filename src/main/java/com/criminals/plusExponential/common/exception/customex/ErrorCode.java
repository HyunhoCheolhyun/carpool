package com.criminals.plusExponential.common.exception.customex;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //104
    TooCloseBetweenInitAndDestination("출발지와 목적지 사이 거리가 너무 가깝습니다."),


    ; //지우지 마셈

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
