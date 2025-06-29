package com.criminals.plusExponential.presentation.user;

import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.application.user.AuthService;
import com.criminals.plusExponential.application.validator.UserValidators;
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
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final UserValidators.EmailValidator emailValidator;



    @InitBinder
    public void validatorBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }


    @PostMapping("/passenger")
    public String joinProcPassenger(@Valid @ModelAttribute UserDto.Request dto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = authService.validateHandling(errors);
            model.addAllAttributes(validatorResult);
            return "signup";
        }

        authService.userJoinAsPassenger(dto);
        return "home";
    }

    @PostMapping("/driver")
    public String joinProcDriver(@Valid @RequestBody UserDto.Request dto, Errors errors, Model model) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = authService.validateHandling(errors);
            model.addAllAttributes(validatorResult);
            return "signup";
        }

        authService.userJoinAsDriver(dto);
        return "home";
    }

}
