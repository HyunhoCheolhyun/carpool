package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
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


    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(customUserDetails.getUser().getRole() == Role.PASSENGER){
            return "home";
        }
        else{
            return "driver";
        }
    }

    @GetMapping("/matched")
    public String matched(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(value = "pg_token") String pgToken) {
        redisPgTokenRepository.publishPaymentToken(customUserDetails.getUserId(),pgToken);
        return "matched";
    }
}
