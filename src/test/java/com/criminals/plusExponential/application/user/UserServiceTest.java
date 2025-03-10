package com.criminals.plusExponential.application.user;

import com.criminals.plusExponential.application.dto.UserDto;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    void 승객으로_회원가입한_유저가_db에_들어가_있는지_그리고_그_유저의Role이_PASSENGER인지() {
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
}
