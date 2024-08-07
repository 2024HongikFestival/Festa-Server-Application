package com.hyyh.festa.service;

import com.hyyh.festa.dto.BoothLikeSseResponse;
import com.hyyh.festa.repository.BoothRepository;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class SseService {
    /*
     * 주로 순회가 일어나는 용도로 사용할 때는 안전한 스레드 처리를 위해 CopyOnWriteArrayList를 사용
     */
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private final BoothRepository boothRepository;

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    public void sendEvents() {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(boothRepository.findAll().stream().map(BoothLikeSseResponse::of).collect(Collectors.toList()));
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}