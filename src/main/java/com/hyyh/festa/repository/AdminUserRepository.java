package com.hyyh.festa.repository;

import com.hyyh.festa.domain.AdminUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdminUserRepository extends CrudRepository<AdminUser, Long> {
    Optional<AdminUser> findByUsername(String username);
}
