package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.application.user.UserService;
import com.criminals.plusExponential.application.validator.CustomValidators;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    private final CustomValidators.EmailValidator emailValidator;

    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }



    @PostMapping("/passenger")
    public ResponseEntity<?> joinProcPassenger(@Valid @RequestBody UserDto.Request dto, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = userService.validateHandling(errors);
            return ResponseEntity.badRequest().body(validatorResult);
        }

        userService.userJoinAsPassenger(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료!");
    }


}
