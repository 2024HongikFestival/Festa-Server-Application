package com.hyyh.festa.controller;


import com.hyyh.festa.dto.EntryResponse;
import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.service.PrizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PrizeController {

    private final PrizeService prizeService;

    @PostMapping("/admin/draw")
    public ResponseEntity<ResponseDTO<?>> drawAllWinners(@RequestParam("prize") String prize) {
        try {
            List<EntryResponse> winners = prizeService.drawAllWinners(prize);
            ResponseDTO<?> responseDTO = ResponseDTO.ok("전체 추첨 성공", winners);
            return ResponseEntity.status(200).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    }

    @PostMapping("/admin/draw-one")
    public ResponseEntity<ResponseDTO<?>> drawOneWinner(@RequestParam("prize") String prize) {
        try {
            EntryResponse winner = prizeService.drawOneWinner(prize);
            ResponseDTO<?> responseDTO = ResponseDTO.ok("1인 추가 추첨 성공", winner);
            return ResponseEntity.status(200).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    }
}
