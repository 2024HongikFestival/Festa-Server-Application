package com.hyyh.festa.repository;

import com.hyyh.festa.domain.Lost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LostRepository extends JpaRepository<Lost, Long> {

    Page<Lost> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}