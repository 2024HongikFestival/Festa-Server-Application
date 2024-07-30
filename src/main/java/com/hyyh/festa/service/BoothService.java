package com.hyyh.festa.service;

import com.hyyh.festa.domain.booth.Booth;
import com.hyyh.festa.repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;
    private final SseService sseService;

    @Transactional
    public void likeBooth(String boothName) {
        Booth booth = boothRepository.findByBoothName(boothName);
        booth.plusLikeCount();
        sseService.sendEvents();

    }
}
