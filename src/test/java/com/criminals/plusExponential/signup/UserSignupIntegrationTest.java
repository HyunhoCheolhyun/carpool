package com.criminals.plusExponential.signup;

import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserSignupIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setup() {
        // 테스트 전에 기존 데이터를 초기화합니다.
        userRepository.deleteAll();
    }

    /**
     * 1. 정상 회원가입 케이스
     * 올바른 값이 전달되면, 회원가입 후 로그인 페이지로 리다이렉션되어야 합니다.
     */
    @Test
    public void testUserSignup_Success() throws Exception {
        mockMvc.perform(post("/auth/joinProcPassenger")
                        .with(csrf())  // CSRF 토큰 추가
                        .param("username", "testUser")
                        .param("password", "Passw0rd!")  // 8~16자, 영문, 숫자, 특수문자 포함
                        .param("email", "test@example.com")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        userRepository.flush();
        entityManager.clear();
        Optional<User> user = userRepository.findByEmail("test@example.com");
        assertTrue(user.isPresent());
        User targetUser = user.get();
        assertEquals(Role.PASSENGER, targetUser.getRole());
    }

}
