package com.hyyh.festa.service;

import com.hyyh.festa.repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final BoothService boothService;
    private final SseService sseService;
    private final BoothRepository boothRepository;

    @Scheduled(cron = "0 0 5 * * *")
    // 매일 오전 5시에 좋아요 초기화
    public void initLikeCount() {
        boothService.initalizeLikeCount();
    }

    // 매 시간마다 실행
    @Scheduled(cron = "0 0 * * * *")
    public void setRankingTask() {
        // 랭킹을 구합니다 .
        boothService.setRankings();
    }

    @Scheduled(fixedRate = 1000)
    public void eventsInvoker() {
        sseService.sendEvents();
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void saveCachedBoothsToDb() {
        // 10초마다 캐시된 부스 객체를 DB에 저장
        boothRepository.saveAll(BoothService.cachedBooths.values());
    }

}
