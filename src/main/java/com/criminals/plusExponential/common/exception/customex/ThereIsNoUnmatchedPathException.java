package com.criminals.plusExponential.common.exception.customex;

public class ThereIsNoUnmatchedPathException extends OurServiceException {

    public ThereIsNoUnmatchedPathException(ErrorCode errorCode) {
        super(errorCode);
    }
}
