package com.criminals.plusExponential.common.exception.dto;

import com.criminals.plusExponential.common.exception.customex.ErrorCode;

public record ErrorResponse(
        ErrorCode errorCode,
        String message


) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode, errorCode.getMessage());
    }
    public static ErrorResponse from(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode, message);
    }

}
