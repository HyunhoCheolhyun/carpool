package com.criminals.plusExponential.application.unmatchedPath;

import com.criminals.plusExponential.common.exception.customex.ThereIsNoUnmatchedPathException;
import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UnmatchedPathServiceTest {


    @Autowired
    UnmatchedPathService unmatchedPathService;

    @Autowired
    UnmatchedPathRepository unmatchedPathRepository;

    @Test
    void sendMessageToMatchingProxyExceptionTest() {

        UnmatchedPath unmatchedPath = new UnmatchedPath();

        unmatchedPath.setId(100L);

        User user = new User();
        user.setId(100L);

        ThereIsNoUnmatchedPathException exception = Assertions.assertThrows(ThereIsNoUnmatchedPathException.class, () -> {
            unmatchedPathService.sendMessageToMatchingProxy(user);
        });
    }


}
