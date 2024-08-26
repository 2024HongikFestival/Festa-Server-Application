package com.hyyh.festa.repository;

import com.hyyh.festa.domain.Booth;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Long> {

    List<Booth> findTop3ByOrderByTotalLikeDesc();

}
