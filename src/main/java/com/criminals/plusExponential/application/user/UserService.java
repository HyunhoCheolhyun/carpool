package com.criminals.plusExponential.application.user;

import com.criminals.plusExponential.infrastructure.persistence.UserRepository;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
