package com.hyyh.festa.repository;

import com.hyyh.festa.domain.booth.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothRepository extends JpaRepository<Booth, Long> {

    Booth findByBoothName(String boothName);
}
