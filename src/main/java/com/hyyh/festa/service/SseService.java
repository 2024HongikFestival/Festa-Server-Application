package com.hyyh.festa.service;

import com.hyyh.festa.domain.Booth;
import com.hyyh.festa.domain.BoothPart;
import com.hyyh.festa.dto.BoothLikeSseResponse;
import com.hyyh.festa.repository.BoothRepository;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class SseService {
    /*
     * 주로 순회가 일어나는 용도로 사용할 때는 안전한 스레드 처리를 위해 CopyOnWriteArrayList를 사용
     */
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private final BoothRepository boothRepository;


    private LocalDateTime latestEventSentAt = LocalDateTime.now();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    @Transactional
    public void sendEvents() {

        Collection<Booth> booths = BoothService.cachedBooths.values();
        Map<String, List<BoothLikeSseResponse>> response = new HashMap<>();

        // BoothLikeSseResponse 객체 리스트를 생성
        for (BoothPart boothPart: BoothPart.values()) {
            response.put(boothPart.getValue(), booths.stream()
                    .filter(b -> b.getBoothPart() == boothPart)
                    .map(BoothLikeSseResponse::of)
                    .toList());
        }

        // 모든 부스의 이전 like 값을 현재 like 값으로 갱신
        booths.forEach(booth -> booth.setPreviousLike(booth.getTotalLike()));

        this.latestEventSentAt = LocalDateTime.now();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(response);
                this.latestEventSentAt = LocalDateTime.now();
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }

    public void sendEventsBatched() {
        if (this.isIncreasedLikeGreaterThen(3)) {
            sendEvents();
        }
    }

    private boolean isIncreasedLikeGreaterThen(int threshold) {

        return BoothService.cachedBooths.values().stream()
                .anyMatch(booth -> booth.getTotalLike() - booth.getPreviousLike() >= threshold);
    }
}