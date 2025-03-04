package com.criminals.plusExponential.application.services.user;

import com.criminals.plusExponential.repository.UserRepository;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
