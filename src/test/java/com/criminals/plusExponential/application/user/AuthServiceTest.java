package com.criminals.plusExponential.application.user;

import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnmatchedPathRepository unmatchedPathRepository;



    @Test
    @Rollback(false)
    void userJoinAsPassenger() {

//        //given
//        UserDto.Request dto = new UserDto.Request();
//        dto.setEmail("test124456@example.com");
//        dto.setPassword("plainPassword123");
//        dto.setUsername("홍길동");
//
//        //when
//        userService.userJoinAsPassenger(dto);
//
//        //then
//        User savedUser = userRepository.findByEmail("test@example.com").orElse(null);
//        assertThat(savedUser).isNotNull();
//        assertThat(savedUser.getRole()).isEqualTo(Role.PASSENGER);
//
//        // 비밀번호가 인코딩되어 저장되는지 확인
//        assertThat(savedUser.getPassword()).isNotEqualTo("plainPassword123");

    }

    @Test
    void userJoinAsDriver() {
        //given

        UserDto.Request dto = new UserDto.Request();
        dto.setEmail("test123@example.com");
        dto.setPassword("plainPassword456");
        dto.setUsername("길동홍");

        //when
        authService.userJoinAsDriver(dto);


        //then
        User savedUser = userRepository.findByEmail("test123@example.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(Role.DRIVER);

        assertThat(savedUser.getPassword()).isNotEqualTo("plainPassword456");

    }


}
