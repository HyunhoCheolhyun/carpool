package com.criminals.plusExponential.infrastructure.persistence;

import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnmatchedPathRepository extends JpaRepository<UnmatchedPath, Long> {

    Optional<UnmatchedPath> findByUser(User user);
}
