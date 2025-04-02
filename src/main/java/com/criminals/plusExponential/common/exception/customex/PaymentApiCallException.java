package com.criminals.plusExponential.common.exception.customex;

public class PaymentApiCallException extends OurServiceException {
    public PaymentApiCallException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
