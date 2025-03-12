package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.application.unmatchedPath.UnmatchedPathService;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/unmatchedPath")
public class UnmatchedPathController {

    private final UnmatchedPathService unmatchedPathService;

    @PostMapping
    public ResponseEntity<?> createUnmatchedPath(@RequestBody UnmatchedPathDto unmatchedPathDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        unmatchedPathService.createUnmatchedPath(unmatchedPathDto, customUserDetails);


        return ResponseEntity.status(HttpStatus.OK).body("unmatchedPath is created!");
    }
}
