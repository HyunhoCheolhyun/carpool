package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if(customUserDetails.getUser().getRole() == Role.PASSENGER){
            return "home";
        }
        else{
            return "driver";
        }
    }
}
