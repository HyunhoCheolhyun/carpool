package com.criminals.plusExponential.infrastructure.persistence;

import com.criminals.plusExponential.domain.entity.MatchedPath;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchedPathRepository extends JpaRepository<MatchedPath, Long> {

    @EntityGraph(attributePaths = {"privateMatchedPaths", "privateMatchedPaths.user"})
    Optional<MatchedPath> findUsersById(Long id);
}
