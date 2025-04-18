package com.criminals.plusExponential.common.exception.customex;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //104
    TooCloseBetweenInitAndDestination("출발지와 목적지 사이 거리가 너무 가깝습니다."),
    LoadSearchFail("출발지와 목적지를 올바르게 입력해주세요"),
    ThereIsNoUnmatchedPath("출발지와 목적지를 설정 한 후 다시 시도해주세요"),
    DecideOrderError("matchedPathService에서 decideOrder메서드 작동 불능"),


    PaymentApiCallException("결제 URL 요청 중에 문제가 발생했습니다"),
    ApproveApiCallException("결제승인 요청 중에 문제가 발생했습니다"),
    PaymentTimeOutException("결제대기시간을 초과 하였습니다."),
    SocketDisconnectedException("사용자와 연결이 끊겼습니다."),
    NotFoundException("해당 리소스를 찾을 수 없습니다."),
    AlreadyMatchedException("이미 매칭되었습니다."),
    FailCreateMathcedPath("경로를 다시 설정해 주세요")
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
