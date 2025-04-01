package com.criminals.plusExponential.application.dto.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveRequestDto {
    String tid;
    String pg_token;
    String cid = "TC0ONETIME";
    private String partner_order_id = "partner_order_id";
    private String partner_user_id = "partner_user_id";

    public ApproveRequestDto(String tid, String pg_token) {
        this.tid = tid;
        this.pg_token = pg_token;
    }
}
