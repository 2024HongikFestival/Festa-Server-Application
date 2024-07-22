package com.hyyh.festa.controller;

import com.hyyh.festa.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final SseService sseService;

    // UTF-8 데이터만 보낼 수 있음, 바이너리 데이터 지원 x
    @GetMapping(path = "/like", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(){
        //SseEmitter는 서버에서 클라이언트로 이벤트를 전달할 수 있습니다.
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseService.addEmitter(emitter);
        return emitter;
    }
}
