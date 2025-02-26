package com.criminals.plusExponential.common.exception.customex;

public class BadRequestException extends OurServiceException {

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
