package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.application.DriverService;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/driver")
public class DriverController {
    private final DriverService driverService;


    @GetMapping("/accept/{matchedPathId}")
    public ResponseEntity<Void> accept(@PathVariable("matchedPathId") Long matchedPathId, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws InterruptedException {
        return driverService.accept(matchedPathId,customUserDetails.getUser());
    }
}
