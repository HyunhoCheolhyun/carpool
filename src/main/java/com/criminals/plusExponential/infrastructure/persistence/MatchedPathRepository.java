package com.criminals.plusExponential.infrastructure.persistence;

import com.criminals.plusExponential.domain.entity.MatchedPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchedPathRepository extends JpaRepository<MatchedPath, Long> {

}
