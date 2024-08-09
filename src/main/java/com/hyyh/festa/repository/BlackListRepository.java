package com.hyyh.festa.repository;

import com.hyyh.festa.domain.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList,Long> {
    boolean existsByFestaUserKakaoSub(String KakaoSub);
    void deleteByFestaUserKakaoSub(String kakaoSub);
}