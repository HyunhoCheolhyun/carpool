package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.application.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/driver")
public class DriverController {
    private final DriverService driverService;


    @GetMapping("/accept/{matchedPathId}")
    public ResponseEntity<Void> accept(@PathVariable("matchedPathId") Long matchedPathId) throws InterruptedException {
        return driverService.accept(matchedPathId);
    }
}
