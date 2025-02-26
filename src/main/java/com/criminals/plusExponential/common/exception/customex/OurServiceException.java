package com.criminals.plusExponential.common.exception.customex;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public abstract class OurServiceException extends NestedRuntimeException {

    private final ErrorCode errorCode;

    protected OurServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected OurServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected OurServiceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
