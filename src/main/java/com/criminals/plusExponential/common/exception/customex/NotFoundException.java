package com.criminals.plusExponential.common.exception.customex;

public class NotFoundException extends OurServiceException{

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
