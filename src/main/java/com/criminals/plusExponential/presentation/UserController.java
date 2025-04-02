package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.application.user.UserService;
import com.criminals.plusExponential.application.validator.UserValidators;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.infrastructure.socket.WebSocketDriverService;
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
public class UserController {

    private final UserService userService;

    private final UserValidators.EmailValidator emailValidator;

    private final WebSocketDriverService webSocketDriverService;

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

    @PostMapping("/driver")
    public ResponseEntity<?> joinProcDriver(@Valid @RequestBody UserDto.Request dto, Errors errors) {
        if (errors.hasErrors()) {
            Map<String, String> validatorResult = userService.validateHandling(errors);
            return ResponseEntity.badRequest().body(validatorResult);
        }

        userService.userJoinAsDriver(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 완료!");
    }
    @GetMapping("/test")
    public void test(){
        MatchedPath matchedPath = new MatchedPath();
        matchedPath.setInitPoint(new Coordinate(37.2094, 126.9769)); //수원대학교
        matchedPath.setDestinationPoint(new Coordinate(37.3463, 126.9395)); //당동초등학교
        matchedPath.setId(123L);

        webSocketDriverService.sendAllDriver(matchedPath,5);
    }
}
