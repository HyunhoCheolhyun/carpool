package com.criminals.plusExponential.common.exception.customex;

public class ApproveApiCallException extends OurServiceException {
    public ApproveApiCallException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
