package com.hyyh.festa.service;

import com.hyyh.festa.repository.BlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;

    public boolean isUserBlocked(String festaUserId){
        return blackListRepository.existsByFestaUserKakaoSub(festaUserId);
    }
}
