package com.criminals.plusExponential.presentation.user;

import com.criminals.plusExponential.application.dto.history.PassengerHistoryResponseDto;
import com.criminals.plusExponential.application.history.HistoryService;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final HistoryService historyService;

    @GetMapping("/passenger/history")
    public ResponseEntity<List<PassengerHistoryResponseDto>> getHistories(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = customUserDetails.getUser();

        List<PassengerHistoryResponseDto> passengerHistory = historyService.getPassengerHistory(user);

        return ResponseEntity.ok(passengerHistory);

    }
}
