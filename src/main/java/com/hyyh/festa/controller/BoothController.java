package com.hyyh.festa.controller;

import com.hyyh.festa.dto.BoothLikeRequest;
import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.service.BoothService;
import com.hyyh.festa.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class BoothController {

    private final BoothService boothService;
    private final SseService sseService;


    @PostMapping("/booth/like")
    public ResponseDTO likeBooth(@RequestBody final BoothLikeRequest likeBoothDto) {

        boothService.likeBooth(likeBoothDto.boothName());
        return ResponseDTO.ok("좋아요 성공", likeBoothDto.boothName());
    }

    // UTF-8 데이터만 보낼 수 있음, 바이너리 데이터 지원 x
    @GetMapping(path = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(){
        //SseEmitter는 서버에서 클라이언트로 이벤트를 전달할 수 있습니다.
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        sseService.addEmitter(emitter);
        return emitter;
    }

    @GetMapping("/booth")
    public ResponseDTO getBooth() {

        return ResponseDTO.ok("주점 리스트 조회 성공", boothService.getBooth());
    }

}


