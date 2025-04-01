package com.criminals.plusExponential.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/driver")
public class DriverController {

    /**
     * 택시기사 배차수락
     * 1. 이미 배차됐는지 확인
     * 2. 카카오페이결제 URL 받아오기 (Tid저장)
     * 3. 승객들에게 URL전달하고 결제완료될때까지 폴링
     * 4. 택시기사에게 매칭완료 넘겨주기
     */
    @PostMapping("/accept")
    public void accept(){

    }
}
