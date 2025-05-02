package com.hyyh.festa.service;

import com.hyyh.festa.domain.Booth;
import com.hyyh.festa.dto.BoothGetResponse;
import com.hyyh.festa.dto.BoothRankingGetResponse;
import com.hyyh.festa.repository.BoothRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;
    private final SseService sseService;

    // 캐시된 부스 객체를 ID로 관리
    public static final Map<Long, Booth> cachedBooths = new ConcurrentHashMap<>();

    // 부스 랭킹 캐시
    public List<Booth> rankings;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        // 컴포넌트 생성 시 캐시 및 rankings 초기화
        boothRepository.findAll()
                .forEach(booth -> cachedBooths.put(booth.getId(), booth));
        // 컴포넌트 생성시 rankings 초기화
        rankings = boothRepository.findTop3ByOrderByTotalLikeDesc();
    }

    @Transactional
    public Booth likeBooth(Long boothId) {
        Booth booth = cachedBooths.get(boothId);
        booth.plusLikeCount();

        // SSE 이벤트 배치 전송
        sseService.sendEventsBatched();

        return booth;
    }

    public List<BoothGetResponse> getBooths() {
        // 캐시된 부스 리스트를 기반으로 응답 생성
        return cachedBooths.values().stream().map(BoothGetResponse::of).collect(Collectors.toList());
    }

    public Booth getBooth(Long boothId) {

        Booth booth = cachedBooths.get(boothId);
        return booth;
    }

    @Transactional
    public void initalizeLikeCount() {
        // 캐시된 부스 객체 초기화
        cachedBooths.values().forEach(booth -> {
            booth.setPreviousLike(0);
            booth.setTotalLike(0);
        });
    }

    public List<BoothRankingGetResponse> getBoothsByRanking() {
        // 캐시된 rankings 리스트를 기반으로 응답 생성
        return rankings.stream().map(BoothRankingGetResponse::of).collect(Collectors.toList());
    }
    public void setRankings() {
        // 부스 순위 업데이트
        rankings = boothRepository.findTop3ByOrderByTotalLikeDesc();
    }

}
