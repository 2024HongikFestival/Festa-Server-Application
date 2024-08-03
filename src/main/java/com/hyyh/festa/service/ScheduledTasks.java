package com.hyyh.festa.service;

import com.hyyh.festa.domain.booth.Booth;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final BoothService boothService;

    @Scheduled(cron = "0 0 5 * * *")
    // 매일 오전 5시에 좋아요 초기화
    public void initLikeCount() {
        boothService.initalizeLikeCount();
    }
}
