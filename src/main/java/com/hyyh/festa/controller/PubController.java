package com.hyyh.festa.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주점", description = "주점 관련하여 CRUD를 담당하는 API 입니다.")
@RequestMapping
@RestController
public interface PubController {
    @Operation(summary = "리스트뷰 일기 조회 ", description = "QueryString 을 이용해 일기(리스트뷰)조회를 합니다.")
    @PostMapping("/{pubId}/like")
    ResponseEntity<?> likePub(@PathVariable("pubId") @Parameter(name = "주점Id", description = "좋아요 할 주점 Id", required = true) Integer pubId);
}
