package com.criminals.plusExponential.common.exception.customex;

public class AlreadyMatchedException extends OurServiceException{
    public AlreadyMatchedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AlreadyMatchedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AlreadyMatchedException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
