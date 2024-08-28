package com.hyyh.festa.repository;

import com.hyyh.festa.domain.Lost;
import com.hyyh.festa.domain.LostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LostRepository extends JpaRepository<Lost, Long> {
    List<Lost> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Lost> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    List<Lost> findAllByLostStatus(LostStatus lostStatus);
    List<Lost> findAllByLostStatus(LostStatus lostStatus, Pageable pageable);
    List<Lost> findAllByLostStatusAndCreatedAtBetween(LostStatus lostStatus, LocalDateTime start, LocalDateTime end);
    List<Lost> findAllByLostStatusAndCreatedAtBetween(LostStatus lostStatus, LocalDateTime start, LocalDateTime end, Pageable pageable);

}