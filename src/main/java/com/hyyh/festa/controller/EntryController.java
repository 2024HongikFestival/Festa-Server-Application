package com.hyyh.festa.controller;

import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.dto.EntryPostRequest;
import com.hyyh.festa.dto.EntryResponse;
import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.repository.FestaUserRepository;
import com.hyyh.festa.service.EntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;
    private final FestaUserRepository festaUserRepository;

    @PostMapping("/events/{eventId}/entries")
    public ResponseEntity<ResponseDTO<?>> createEntry(@PathVariable Long eventId, @Valid @RequestBody EntryPostRequest entryPostRequest) {
        try{
            FestaUser festaUser = festaUserRepository.findByKakaoSub(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
            if (festaUser == null) {
                ResponseDTO<?> responseDTO = ResponseDTO.notFound("토큰에서 festaUser를 찾는 중 오류가 발생했습니다.");
                return ResponseEntity.status(404).body(responseDTO);
            }
            EntryResponse createdEntry = entryService.createEntry(eventId, festaUser, entryPostRequest);
            ResponseDTO<?> responseDTO = ResponseDTO.created("이벤트에 응모하기", createdEntry);
            return ResponseEntity.status(201).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    }

    @GetMapping("/admin/events/{eventId}/entries/{entryId}")
    public ResponseEntity<ResponseDTO<?>> getEntryById(@PathVariable Long eventId, @PathVariable Long entryId) {
        try {
            EntryResponse entry = entryService.getEntryById(eventId, entryId);
            ResponseDTO<?> responseDTO = ResponseDTO.ok("응모자 단건 조회 성공", entry);
            return ResponseEntity.status(200).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        } catch (Exception e) {
            ResponseDTO<?> responseDTO = ResponseDTO.internalServerError(e.getMessage());
            return ResponseEntity.status(500).body(responseDTO);
        }
    }

    @GetMapping("/admin/events/{eventId}/entries")
    public ResponseEntity<ResponseDTO<?>> getEntriesByEventId(@PathVariable Long eventId) {
        try {
            List<EntryResponse> entries = entryService.getEntriesByEventId(eventId);
            ResponseDTO<?> responseDTO = ResponseDTO.ok("응모자 리스트 조회 성공", entries);
            return ResponseEntity.status(200).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    }
}
