package com.hyyh.festa.service;

import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.domain.Lost;
import com.hyyh.festa.domain.LostStatus;
import com.hyyh.festa.dto.GetAdminLostDTO;
import com.hyyh.festa.dto.GetUserLostDTO;
import com.hyyh.festa.dto.LostRequestDTO;
import com.hyyh.festa.repository.BlackListRepository;
import com.hyyh.festa.repository.FestaUserRepository;
import com.hyyh.festa.repository.LostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LostService {

    private final LostRepository lostRepository;
    private final FestaUserRepository festaUserRepository;
    private final BlackListRepository blackListRepository;
    private final BlackListService blackListService;

    private final int pageItemCount = 9;

    public GetAdminLostDTO createLost(UserDetails userDetails, LostRequestDTO lostRequestDTO) {
        String username = userDetails.getUsername();
        FestaUser festaUser = festaUserRepository.findByKakaoSub(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자id"));

        if (blackListRepository.existsByFestaUserKakaoSub(username)) {
            throw new IllegalArgumentException("차단된 사용자입니다.");
        }

        Lost lost = Lost.builder()
                .festaUser(festaUser)
                .foundLocation(lostRequestDTO.getFoundLocation())
                .storageLocation(lostRequestDTO.getStorageLocation())
                .content(lostRequestDTO.getContent())
                .imageUrl(lostRequestDTO.getImageUrl())
                .lostStatus(LostStatus.PUBLISHED)
                .build();

        Lost savedLost = lostRepository.save(lost);

        return mapToAdminDTO(savedLost);
    }

    public Optional<GetAdminLostDTO> getOneAdminLost(Long lostId) {
        return lostRepository.findById(lostId)
                .map(this::mapToAdminDTO);
    }

    public Optional<GetUserLostDTO> getOneUserLost(Long lostId) {
        return lostRepository.findById(lostId)
                .map(this::mapToUserDTO);
    }

    public List<GetAdminLostDTO> getLostListByAdmin(int page, LocalDate date, String userId){
        Pageable pageable = PageRequest.of(page, pageItemCount, Sort.by("createdAt").descending());
        List<GetAdminLostDTO> losts;

        if (date == null) {
            losts = lostRepository.findAll(pageable).stream()
                    .map(this::mapToAdminDTO)
                    .toList();
        } else {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = start.plusDays(1);

            losts = lostRepository.findAllByCreatedAtBetween(start, end, pageable).stream()
                    .map(this::mapToAdminDTO)
                    .toList();
        }

        if (userId == null)
            return losts;
        return losts.stream()
                .filter((lost) -> lost.getUserId().equals(userId))
                .toList();
    }

    public List<GetUserLostDTO> getLostListByUser(int page, LocalDate date){
        Pageable pageable = PageRequest.of(page, pageItemCount, Sort.by("createdAt").descending());

        if (date == null) {
            return lostRepository.findAllByLostStatus(LostStatus.PUBLISHED, pageable).stream()
                    .map(this::mapToUserDTO)
                    .toList();
        } else {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            return lostRepository.findAllByLostStatusAndCreatedAtBetween(LostStatus.PUBLISHED, start, end, pageable).stream()
                    .map(this::mapToUserDTO)
                    .toList();
        }
    }

    public GetAdminLostDTO deleteLost(UserDetails userDetails, Long lostId) {
        Lost lost = lostRepository.findById(lostId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 분실물 게시판id"));
        if (getAuthority(userDetails).equals("ADMIN")) {
            if (lost.getLostStatus() != LostStatus.PUBLISHED)
                throw new IllegalArgumentException("이미 삭제 상태입니다.");
            lost.setLostStatus(LostStatus.DELETED);
            lostRepository.save(lost);
        } else {
            throw new IllegalArgumentException("ADMIN이 아닙니다.");
        }
        return mapToAdminDTO(lost);
    }

    public GetAdminLostDTO putLost(UserDetails userDetails, Long lostId) {
        Lost lost = lostRepository.findById(lostId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 분실물 게시판id"));
        if (getAuthority(userDetails).equals("ADMIN")) {
            if (lost.getLostStatus() != LostStatus.DELETED)
                throw new IllegalArgumentException("이미 게시된 상태입니다.");
            lost.setLostStatus(LostStatus.PUBLISHED);
            lostRepository.save(lost);
        } else {
            throw new IllegalArgumentException("ADMIN이 아닙니다.");
        }
        return mapToAdminDTO(lost);
    }

    private GetAdminLostDTO mapToAdminDTO(Lost lost) {
        boolean isUserBlocked = blackListService.isUserBlocked(lost.getFestaUser().getUsername());

        return GetAdminLostDTO.builder()
                .lostId(lost.getId())
                .lostStatus(lost.getLostStatus()) //PUBLISHED, DELETED
                .userId(lost.getFestaUser().getUsername()) //sub
                .isUserBlocked(isUserBlocked) //ture, fasle
                .foundLocation(lost.getFoundLocation())
                .storageLocation(lost.getStorageLocation())
                .content(lost.getContent())
                .imageUrl(lost.getImageUrl())
                .createdAt(lost.getCreatedAt())
                .build();
    }
    private GetUserLostDTO mapToUserDTO(Lost lost) {
        return GetUserLostDTO.builder()
                .lostId(lost.getId())
                .lostStatus(lost.getLostStatus())
                .foundLocation(lost.getFoundLocation())
                .storageLocation(lost.getStorageLocation())
                .content(lost.getContent())
                .imageUrl(lost.getImageUrl())
                .createdAt(lost.getCreatedAt())
                .build();
    }

    private String getAuthority(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
    }

    public int countTotalPage(LocalDate date) {
        List<Lost> losts;
        if (date == null) {
            losts = lostRepository.findAll();
        } else {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            losts = lostRepository.findAllByCreatedAtBetween(start, end);
        }
        return roundPageCount(losts.size());
    }

    public int countPublishedTotalPage(LocalDate date) {
        List<Lost> losts;
        if (date == null) {
            losts = lostRepository.findAllByLostStatus(LostStatus.PUBLISHED);
        } else {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            losts = lostRepository.findAllByLostStatusAndCreatedAtBetween(LostStatus.PUBLISHED, start, end);
        }
        return roundPageCount(losts.size());
    }

    private int roundPageCount(int itemCount) {
        return itemCount / pageItemCount + (itemCount % pageItemCount != 0 ? 1 : 0);
    }

}
