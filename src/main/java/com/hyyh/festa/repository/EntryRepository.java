package com.hyyh.festa.repository;

import com.hyyh.festa.domain.Entry;
import com.hyyh.festa.domain.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findAllByUserAndDate(UserDetails user, int date);

    List<Entry> findAllByPrize(Prize prize);
}