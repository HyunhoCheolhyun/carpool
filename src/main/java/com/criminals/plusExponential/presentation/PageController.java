package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.persistence.MatchedPathRepository;
import com.criminals.plusExponential.infrastructure.redis.RedisPgTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final RedisPgTokenRepository redisPgTokenRepository;
    private final MatchedPathRepository matchedPathRepository;


    // 홈화면 -> 택시기사, 승객 따로 랜더링
    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(customUserDetails.getUser().getRole() == Role.PASSENGER){
            return "home";
        }
        else{
            return "driver";
        }
    }

    // 결제완료 후 승객 화면
    @GetMapping("/matched")
    public String matched(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(value = "pg_token") String pgToken) {
        redisPgTokenRepository.publishPaymentToken(customUserDetails.getUserId(),pgToken);
        return "match-passenger";
    }

    // 매칭후 택시기사 화면
    @GetMapping("/match-driver/{matchedPathId}")
    public String matched(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("matchedPathId") Long matchedPathId) {
        MatchedPath matchedPath = matchedPathRepository.findById(matchedPathId)
                .orElseThrow(() -> new IllegalArgumentException("해당 경로가 존재하지 않습니다: " + matchedPathId));

        model.addAttribute("departure", "서울역");
        model.addAttribute("waypoint1", "강남역");
        model.addAttribute("waypoint2", "잠실역");
        model.addAttribute("destination", "인천공항");

        return "match-driver";
    }
}
