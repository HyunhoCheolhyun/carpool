package com.criminals.plusExponential.infrastructure.persistence;

import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivateMatchedPathRepository extends JpaRepository<PrivateMatchedPath, Long> {
}
