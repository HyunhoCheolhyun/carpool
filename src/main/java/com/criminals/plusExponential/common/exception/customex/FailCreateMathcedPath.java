package com.criminals.plusExponential.common.exception.customex;

public class FailCreateMathcedPath extends OurServiceException {
    public FailCreateMathcedPath(ErrorCode errorCode) {
        super(errorCode);
    }

    public FailCreateMathcedPath(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
