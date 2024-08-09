package com.hyyh.festa.repository;

import com.hyyh.festa.domain.Entry;
import com.hyyh.festa.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findAllByEventId(Long eventId);

    boolean existsByUserAndEvent(UserDetails festaUser, Event event);
}
