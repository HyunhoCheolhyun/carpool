package com.criminals.plusExponential.presentation.user;

import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.application.user.AuthService;
import com.criminals.plusExponential.application.validator.UserValidators;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final UserValidators.EmailValidator emailValidator;



    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }


    @PostMapping("/passenger")
    public ResponseEntity<?> joinProcPassenger(@Valid @RequestBody UserDto.Request dto, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = authService.validateHandling(errors);
            return ResponseEntity.badRequest().body(validatorResult);
        }

        authService.userJoinAsPassenger(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료!");
    }

    @PostMapping("/driver")
    public ResponseEntity<?> joinProcDriver(@Valid @RequestBody UserDto.Request dto, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = authService.validateHandling(errors);
            return ResponseEntity.badRequest().body(validatorResult);
        }

        authService.userJoinAsDriver(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료!");
    }

}
