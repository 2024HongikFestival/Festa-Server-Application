package com.hyyh.festa.repository;

import com.hyyh.festa.domain.KakaoPublicKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoPublicKeyRepository extends JpaRepository<KakaoPublicKey, Long> {

    Optional<KakaoPublicKey> findByKid(String kid);

}
