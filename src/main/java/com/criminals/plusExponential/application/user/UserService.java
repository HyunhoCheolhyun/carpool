package com.criminals.plusExponential.application.user;

import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UnmatchedPathRepository unmatchedPathRepository;

    @Transactional
    public void userJoinAsPassenger(UserDto.Request dto) {

        dto.setPassword(encoder.encode(dto.getPassword()));
        User user = dto.toEntity();
        user.setRole(Role.PASSENGER);

        userRepository.save(user);

    }

    @Transactional
    public void userJoinAsDriver(UserDto.Request dto) {
        dto.setPassword(encoder.encode(dto.getPassword()));

        User user = dto.toEntity();
        user.setRole(Role.DRIVER);

        userRepository.save(user);

    }



    public Map<String, String> validateHandling(Errors errors) {

        Map<String, String> validatorResult = new HashMap<>();
        for (FieldError fieldError : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s,", fieldError.getField());
            validatorResult.put(validKeyName, fieldError.getDefaultMessage());
        }

        return validatorResult;

    }
}
