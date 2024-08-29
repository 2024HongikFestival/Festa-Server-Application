package com.hyyh.festa.controller;

import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.domain.Prize;
import com.hyyh.festa.dto.EntryPostRequest;
import com.hyyh.festa.dto.EntryResponse;
import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.dto.WinEntryResponse;
import com.hyyh.festa.repository.EntryRepository;
import com.hyyh.festa.repository.FestaUserRepository;
import com.hyyh.festa.service.EntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;
    private final EntryRepository entryRepository;
    private final FestaUserRepository festaUserRepository;

    @PostMapping("/entries")
    public ResponseEntity<ResponseDTO<?>> createEntry(@Valid @RequestBody EntryPostRequest entryPostRequest) {
        try{
            FestaUser festaUser = festaUserRepository.findByKakaoSub(SecurityContextHolder.getContext().getAuthentication().getName()).orElse(null);
            if (festaUser == null) {
                ResponseDTO<?> responseDTO = ResponseDTO.notFound("토큰에서 festaUser를 찾는 중 오류가 발생했습니다.");
                return ResponseEntity.status(404).body(responseDTO);
            }
            EntryResponse createdEntry = entryService.createEntry(festaUser, entryPostRequest);
            ResponseDTO<?> responseDTO = ResponseDTO.created("이벤트에 응모하기", createdEntry);
            return ResponseEntity.status(201).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        } catch (IllegalStateException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.custom(HttpStatus.CONFLICT, e.getMessage(), Collections.emptyMap());
            return ResponseEntity.status(409).body(responseDTO);
        }
    }

    @GetMapping("/entries/prizes")
    public ResponseEntity<ResponseDTO<?>> getPrizes() {
        List<Map<String, Object>> prizeList = new ArrayList<>();

        for (Prize p : Prize.values()) {
            Map<String, Object> prizeInfo = new HashMap<>();
            prizeInfo.put("prizeName", p.prizeName);
            prizeInfo.put("quantity", p.quantity);
            prizeList.add(prizeInfo);
        }

        ResponseDTO<?> responseDTO = ResponseDTO.ok("경품 목록 조회 성공", prizeList);
        return ResponseEntity.status(200).body(responseDTO);
    }

    @GetMapping("/admin/entries/{entryId}")
    public ResponseEntity<ResponseDTO<?>> getEntryById(@PathVariable Long entryId) {
        try {
            EntryResponse entry = entryService.getEntryById(entryId);
            ResponseDTO<?> responseDTO = ResponseDTO.ok("응모 단건 조회 성공", entry);
            return ResponseEntity.status(200).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        } catch (Exception e) {
            ResponseDTO<?> responseDTO = ResponseDTO.internalServerError(e.getMessage());
            return ResponseEntity.status(500).body(responseDTO);
        }
    }

    @GetMapping("/admin/entries")
    public ResponseEntity<ResponseDTO<?>> getEntriesByEventId(@RequestParam(value = "prize", required = true) String prize) {
        try {
            List<EntryResponse> entries = entryService.getEntriesByPrize(prize);
            ResponseDTO<?> responseDTO = ResponseDTO.ok("응모 목록 조회 성공", entries);
            return ResponseEntity.status(200).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound("존재하지 않는 경품입니다.");
            return ResponseEntity.status(404).body(responseDTO);
        }
    }

    @DeleteMapping("/admin/entries/{entryId}")
    public ResponseEntity<ResponseDTO<?>> cancelWinner(@PathVariable Long entryId) {
        try {
            EntryResponse canceledWinner = entryService.cancelWinner(entryId);
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(
                    "당첨 취소 성공");
            return ResponseEntity.status(204).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    }

    @GetMapping("/admin/entries/winners")
    public ResponseEntity<ResponseDTO<?>> getWinners() {
        List<WinEntryResponse> winningEntries = entryService.getWinningEntries();

        ResponseDTO<?> responseDTO = ResponseDTO.ok("당첨자 목록 조회 성공", winningEntries);
        return ResponseEntity.status(200).body(responseDTO);
    }

    @GetMapping("/admin/entries/prizes")
    public ResponseEntity<ResponseDTO<?>> getAdminPrizes() {
        List<Map<String, Object>> prizeList = new ArrayList<>();

        for (Prize p : Prize.values()) {
            Map<String, Object> prizeInfo = new HashMap<>();
            prizeInfo.put("prizeName", p.prizeName);
            prizeInfo.put("quantity", p.quantity);
            prizeInfo.put("entryCount", entryRepository.countByPrize(p));
            prizeInfo.put("drawCompleted", entryService.drawCompleted(p));
            prizeList.add(prizeInfo);
        }

        ResponseDTO<?> responseDTO = ResponseDTO.ok("경품 목록 조회 성공", prizeList);
        return ResponseEntity.status(200).body(responseDTO);
    }
}
