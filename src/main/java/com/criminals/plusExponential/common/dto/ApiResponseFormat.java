package com.criminals.plusExponential.common.dto;

public record ApiResponseFormat(
        String timestamp,
        int status,
        String error,
        String path,
        Object message
) {

}