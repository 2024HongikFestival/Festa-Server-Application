package com.hyyh.festa.repository;

import com.hyyh.festa.domain.Lost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostRepository extends JpaRepository<Lost, Long> {
}
