package com.criminals.plusExponential.common.exception.customex;

import lombok.Getter;

@Getter
public enum ErrorCode {



    ; //지우지 마셈

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
