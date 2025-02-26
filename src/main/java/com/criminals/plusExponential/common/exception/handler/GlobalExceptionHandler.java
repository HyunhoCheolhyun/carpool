package com.criminals.plusExponential.common.exception.handler;

import com.criminals.plusExponential.common.dto.ApiResponseFormat;
import com.criminals.plusExponential.common.exception.customex.BadRequestException;
import com.criminals.plusExponential.common.exception.customex.ErrorCode;
import com.criminals.plusExponential.common.exception.customex.NotFoundException;
import com.criminals.plusExponential.common.exception.customex.ValidException;
import com.criminals.plusExponential.common.exception.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {


    private static final String LOG_FORMAT_INFO = "\n[🟢INFO] - ({} {}) ({})";
    private static final String LOG_FORMAT_WARN = "\n[🟠WARN] - ({} {}) ({})";
    private static final String LOG_FORMAT_ERROR = "\n[🔴ERROR] - ({} {}) ({})";

    private final HttpServletRequest request;
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponseFormat> handle(BadRequestException e) {
        logWarn(e);
        return ResponseEntity.status(httpStatus)
                .body(getErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseFormat> handle(NotFoundException e) {
        logWarn(e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(getErrorResponse(e.getErrorCode()));
    }

    @ExceptionHandler(ValidException.class)
    public ResponseEntity<ApiResponseFormat> handle(ValidException e) {
        logWarn(e);
        return ResponseEntity.status(httpStatus)
                .body(getErrorResponseWithMessage(e.getErrorCode(), e.getMessage()));
    }

    private void logWarn(Exception e) {
        log.warn(LOG_FORMAT_WARN,
                request.getMethod(), request.getRequestURI(), e.getMessage(), e);
    }

    private ApiResponseFormat getErrorResponse(ErrorCode errorCode) {
        return new ApiResponseFormat(
                LocalDateTime.now().toString(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                request.getRequestURI(),
                ErrorResponse.from(errorCode)
        );
    }
    private ApiResponseFormat getErrorResponseWithMessage(ErrorCode errorCode, String message) {
        return new ApiResponseFormat(
                LocalDateTime.now().toString(),
                httpStatus.value(),
                httpStatus.getReasonPhrase(),
                request.getRequestURI(),
                ErrorResponse.from(errorCode, message)
        );
    }
}
