package com.hyyh.festa.service;

import com.hyyh.festa.domain.BlackList;
import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.dto.BlackListRequestDTO;
import com.hyyh.festa.dto.BlackListResponseDTO;
import com.hyyh.festa.repository.BlackListRepository;
import com.hyyh.festa.repository.FestaUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListService {

    private final BlackListRepository blackListRepository;
    private final FestaUserRepository festaUserRepository;

    public boolean isUserBlocked(String festaUserId){
        return blackListRepository.existsByFestaUserKakaoSub(festaUserId);
    }

    public BlackListResponseDTO addToBlackList(BlackListRequestDTO blackListRequestDTO) {
        if (isUserBlocked(blackListRequestDTO.getUserId())) {
            throw new IllegalArgumentException("이미 블랙리스트에 존재하는 사용자");
        }
        FestaUser festaUser = festaUserRepository.findByKakaoSub(blackListRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));

        BlackList blackList = BlackList.builder()
                .festaUser(festaUser)
                .build();

        BlackList savedBlackList = blackListRepository.save(blackList);

        return toBlackListResponse(savedBlackList);
    }

    public List<BlackListResponseDTO> getAllBlackLists() {
        List<BlackList> blackLists = blackListRepository.findAll();
        return blackLists.stream()
                .map(this::toBlackListResponse)
                .collect(Collectors.toList());
    }

    public void deleteBlackList(String userId) {
        FestaUser festaUser = festaUserRepository.findByKakaoSub(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));
        if (!isUserBlocked(userId)) {
            throw new IllegalArgumentException("블랙리스트에 존재하지 않는 사용자");
        }
        blackListRepository.deleteByFestaUserKakaoSub(userId);
    }

    private BlackListResponseDTO toBlackListResponse(BlackList blackList) {
        return BlackListResponseDTO.builder()
                .userId(blackList.getFestaUser().getUsername())
                .blockedAt(blackList.getBlockedAt())
                .build();
    }
}

