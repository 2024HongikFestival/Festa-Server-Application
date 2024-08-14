package com.hyyh.festa.service;

import com.hyyh.festa.domain.booth.Booth;
import com.hyyh.festa.dto.BoothGetResponse;
import com.hyyh.festa.dto.BoothRankingGetResponse;
import com.hyyh.festa.repository.BoothRepository;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;
    private final SseService sseService;

    public List<Booth> rankings;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // 컴포넌트 생성시 rankings 초기화
        rankings = boothRepository.findTop3ByOrderByTotalLikeDesc();
    }

    @Transactional
    public Booth likeBooth(Long boothId) {
        Booth booth = boothRepository.findByIdForLikeCountUpdate(boothId);
        booth.plusLikeCount();
        sseService.sendEvents();

        return booth;
    }

    public List<BoothGetResponse> getBooths() {
        return boothRepository.findAll().stream().map(BoothGetResponse::of).collect(Collectors.toList());
    }

    public Booth getBooth(Long boothId) {
        return boothRepository.findById(boothId).get();
    }

    @Transactional
    public void initalizeLikeCount() {
        List<Booth> booths = boothRepository.findAll();
        for (Booth booth : booths) {
            booth.setTotalLike(0);
        }
    }

    public List<BoothRankingGetResponse> getBoothsByRanking() {
        return rankings.stream().map(BoothRankingGetResponse::of).collect(Collectors.toList());
    }
    public void setRankings() {
        rankings = boothRepository.findTop3ByOrderByTotalLikeDesc();
    }
}
