package com.hyyh.festa.repository;

import com.hyyh.festa.domain.FestaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FestaUserRepository extends JpaRepository<FestaUser, Long> {

    Optional<FestaUser> findByKakaoSub (String kakaoSub);

}