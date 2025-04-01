package com.criminals.plusExponential.application.dto.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDto {
    String tid;
    String next_redirect_app_url;
    String next_redirect_mobile_url;
    String next_redirect_pc_url;
    String android_app_scheme;
    String ios_app_scheme;
    String created_at;

    @Override
    public String toString() {
        return "PaymentResponseDto{" +
                "tid='" + tid + '\'' +
                ", next_redirect_app_url='" + next_redirect_app_url + '\'' +
                ", next_redirect_mobile_url='" + next_redirect_mobile_url + '\'' +
                ", next_redirect_pc_url='" + next_redirect_pc_url + '\'' +
                ", android_app_scheme='" + android_app_scheme + '\'' +
                ", ios_app_scheme='" + ios_app_scheme + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
