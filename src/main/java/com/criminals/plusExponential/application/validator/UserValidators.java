package com.criminals.plusExponential.application.validator;

import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;


@RequiredArgsConstructor
@Component
public class UserValidators {

    @RequiredArgsConstructor
    @Component
    public static class EmailValidator extends AbstractValidator<UserDto.Request> {

        private final UserRepository userRepository;
        @Override
        protected void doValidate(UserDto.Request dto, Errors errors) {
            if (userRepository.existsByEmail(dto.toEntity().getEmail().trim().toLowerCase())) {
                errors.rejectValue("email", "이메일 중복 오류", "이미 사용중인 이메일 입니다.");
            }
        }
    }
}