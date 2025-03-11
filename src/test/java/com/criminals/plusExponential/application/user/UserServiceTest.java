package com.criminals.plusExponential.application.user;

import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback(false)
    void userJoinAsPassenger() {

        /*
        1.회원가입시 db에 잘 들어가는지
        2.회원가입role과 같은 role이 들어가는지


         */

        //given
        UserDto.Request dto = new UserDto.Request();
        dto.setEmail("test@example.com");
        dto.setPassword("plainPassword123");
        dto.setUsername("홍길동");

        //when
        userService.userJoinAsPassenger(dto);

        //then
        User savedUser = userRepository.findByEmail("test@example.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(Role.PASSENGER);

        // 비밀번호가 인코딩되어 저장되는지 확인
        assertThat(savedUser.getPassword()).isNotEqualTo("plainPassword123");

    }

    @Test
    void userJoinAsDriver() {
        //given

        UserDto.Request dto = new UserDto.Request();
        dto.setEmail("test123@example.com");
        dto.setPassword("plainPassword456");
        dto.setUsername("길동홍");

        //when
        userService.userJoinAsDriver(dto);


        //then
        User savedUser = userRepository.findByEmail("test123@example.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(Role.PASSENGER);

        assertThat(savedUser.getPassword()).isNotEqualTo("plainPassword456");

    }
}
