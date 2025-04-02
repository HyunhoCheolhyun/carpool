package com.criminals.plusExponential.common.exception.customex;

public class PaymentTimeOutException extends OurServiceException {
  public PaymentTimeOutException(ErrorCode errorCode) {
    super(errorCode);
  }
}
