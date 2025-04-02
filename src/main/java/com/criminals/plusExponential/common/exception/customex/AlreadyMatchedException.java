package com.criminals.plusExponential.common.exception.customex;

public class AlreadyMatchedException extends OurServiceException {
    public AlreadyMatchedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
