package com.criminals.plusExponential.application.dto.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    private String cid =  "TC0ONETIME";
    private String partner_order_id = "partner_order_id";
    private String partner_user_id = "partner_user_id";
    private String item_name = "택시요금";
    private String quantity = "1";
    private int total_amount;
    private String tax_free_amount =  "0";

    private String approval_url= "http://localhost:8080/matched";
    private String fail_url= "http://localhost:8080/";
    private String cancel_url= "http://localhost:8080/";

    public PaymentRequestDto(int a){
        total_amount = a;
    }
}
