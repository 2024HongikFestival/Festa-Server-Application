package com.hyyh.festa.repository;

import com.hyyh.festa.domain.Lost;
import com.hyyh.festa.domain.LostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LostRepository extends JpaRepository<Lost, Long> {
    Page<Lost> findAllByLostStatus(LostStatus lostStatus, Pageable pageable);
    Page<Lost> findAllByCreatedAtBetweenAndLostStatus(LocalDateTime start, LocalDateTime end, LostStatus lostStatus, Pageable pageable);
    Page<Lost> findAllByFestaUserKakaoSub(String userId, Pageable pageable);
    Page<Lost> findAllByCreatedAtBetweenAndFestaUserKakaoSub(LocalDateTime start, LocalDateTime end, String userId, Pageable pageable);
}