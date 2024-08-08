package com.hyyh.festa.service;

import com.hyyh.festa.domain.Lost;
import com.hyyh.festa.dto.GetAdminLostDTO;
import com.hyyh.festa.dto.GetUserLostDTO;
import com.hyyh.festa.repository.BlackListRepository;
import com.hyyh.festa.repository.LostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostService {

    private final LostRepository lostRepository;
    private final BlackListRepository blackListRepository;
    private final BlackListService blackListService;

    public Optional<GetAdminLostDTO> getOneAdminLost(Long lostId) {
        return lostRepository.findById(lostId)
                .map(this::mapToAdminDTO);
    }

    public Optional<GetUserLostDTO> getOneUserLost(Long lostId) {
        return lostRepository.findById(lostId)
                .map(this::mapToUserDTO);
    }

    public List<GetAdminLostDTO> getListAdminLost(int page, LocalDate date, String userId){
        return getListLostItems(page, date, userId, this::mapToAdminDTO);
    }
    public List<GetUserLostDTO> getListUserLost(int page, LocalDate date){

        return getListLostItems(page, date,null, this::mapToUserDTO);
    }

    private <T> List<T> getListLostItems(int page, LocalDate date, String userId, Function<Lost, T> mapper) {
        Pageable pageable = PageRequest.of(page, 12, Sort.by("createdAt").descending());

        List<Lost> lostList;
        if (date == null) {
            if (userId == null){
                lostList = lostRepository.findAll(pageable).getContent();
            } else{
                lostList = lostRepository.findAllByFestaUserKakaoSub(userId, pageable).getContent();
            }
        }
        else {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
            if(userId == null){
                lostList = lostRepository.findAllByCreatedAtBetween(startOfDay, endOfDay, pageable).getContent();
            } else{
                lostList = lostRepository.findAllByCreatedAtBetweenAndFestaUserKakaoSub(startOfDay, endOfDay, userId, pageable).getContent();
            }
        }
        return lostList.stream().map(mapper).collect(Collectors.toList());
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
}
