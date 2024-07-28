package com.hyyh.festa.repository;

import com.hyyh.festa.domain.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {
    Optional<JwtBlacklist> findByToken(String token);
}
