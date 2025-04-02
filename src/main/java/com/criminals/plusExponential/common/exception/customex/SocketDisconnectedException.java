package com.criminals.plusExponential.common.exception.customex;

public class SocketDisconnectedException extends OurServiceException {
    public SocketDisconnectedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
