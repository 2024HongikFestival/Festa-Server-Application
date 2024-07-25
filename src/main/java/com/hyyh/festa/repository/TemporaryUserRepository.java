package com.hyyh.festa.repository;

import com.hyyh.festa.domain.TemporaryUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemporaryUserRepository extends CrudRepository<TemporaryUser, String> {
    Optional<TemporaryUser> findBySubject(String subject);
}
