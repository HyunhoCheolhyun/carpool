package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.application.unmatchedPath.UnmatchedPathService;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.redis.RedisPgTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/unmatched-path")
public class UnmatchedPathController {

    private final UnmatchedPathService unmatchedPathService;
    private final RedisPgTokenRepository redisPgTokenRepository;

    /* 실제 서비스에서는 엔드유저가 출발지와 목적지를 지명 혹은 주소로 입력을 주었을 때 프론트단에서 사용하는 kakao map API에서 지명이나 주소를 좌표값으로 바꿔주면서 validation을 하기 때문에  여기에서는 따로 입력 좌표에 대한 validation을 하지 않음 */

    @PostMapping
    public ResponseEntity<?> createUnmatchedPath(@RequestBody UnmatchedPathDto unmatchedPathDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        unmatchedPathService.createUnmatchedPath(unmatchedPathDto, customUserDetails);


        return ResponseEntity.status(HttpStatus.OK).body("unmatchedPath is created!");
    }

    @PostMapping("/init-matching")
    public ResponseEntity<?> sendMessageToMatchingProxy(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws ExecutionException, InterruptedException {

        User user = customUserDetails.getUser();
        unmatchedPathService.sendMessageToMatchingProxy(user);

        return ResponseEntity.status(HttpStatus.OK).body("매칭 프록시에 책임 전달 완료");
    }

}
