package com.criminals.plusExponential.infrastructure.persistence;

import com.criminals.plusExponential.domain.entity.UnmatchedPath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnmatchedPathRepository extends JpaRepository<UnmatchedPath, Long> {


}
