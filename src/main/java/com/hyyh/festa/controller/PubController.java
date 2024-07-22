package com.hyyh.festa.controller;

import com.hyyh.festa.repository.PubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PubController {

    private final PubRepository pubRepository;

    @PostMapping("/{pubId}/like")
    public ResponseEntity<?> likePub(@PathVariable("pubId") Integer pubId) {

        /*
         * pubId 에 해당하는 주점의 좋아요 수를 증가시키는 로직
         *
         * */
        pubRepository.plusLike(pubId);

        return ResponseEntity.status(200).body("좋아요 성공");
    }
}
