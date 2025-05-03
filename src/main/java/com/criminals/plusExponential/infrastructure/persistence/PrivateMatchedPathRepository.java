package com.criminals.plusExponential.infrastructure.persistence;

import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateMatchedPathRepository extends JpaRepository<PrivateMatchedPath, Long> {
    List<PrivateMatchedPath> findByUserOrderByCreatedAtDesc(User user);
}
