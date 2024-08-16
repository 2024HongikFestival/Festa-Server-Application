package com.hyyh.festa.controller;

import com.hyyh.festa.dto.BlackListRequestDTO;
import com.hyyh.festa.dto.BlackListResponseDTO;
import com.hyyh.festa.dto.GetAdminLostDTO;
import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.service.BlackListService;
import com.hyyh.festa.service.LostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class LostAdminController {
    private final LostService lostService;
    private final BlackListService blackListService;

    @DeleteMapping("/losts/{lostId}")
    public ResponseEntity<ResponseDTO<?>> deleteLost(@PathVariable Long lostId,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {
            GetAdminLostDTO deletedLost = lostService.deleteLost(userDetails, lostId);
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(
                    "분실물 게시글 삭제 성공");
            return ResponseEntity.status(204).body(responseDTO);
        } catch (IllegalArgumentException e) {
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    }

    @PostMapping("/blacklist")
    public ResponseEntity<ResponseDTO<?>> addToBlackList(@Valid @RequestBody BlackListRequestDTO blackListRequestDTO) {
        try {
            BlackListResponseDTO createdBlackList = blackListService.addToBlackList(blackListRequestDTO);
            ResponseDTO<?> responseDTO = ResponseDTO.created("블랙 리스트 추가 성공", createdBlackList);
            return ResponseEntity.status(201).body(responseDTO);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("이미 블랙리스트에 존재하는 사용자")) {
                ResponseDTO<?> responseDTO = ResponseDTO.badRequest(e.getMessage());
                return ResponseEntity.status(400).body(responseDTO);
            }
            ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
            return ResponseEntity.status(404).body(responseDTO);
        }
    }

    @GetMapping("/blacklist")
    public ResponseEntity<ResponseDTO<?>> getAllBlackLists() {
        List<BlackListResponseDTO> blackLists = blackListService.getAllBlackLists();
        ResponseDTO<?> responseDTO = ResponseDTO.ok("블랙리스트 조회 성공", blackLists);
        return ResponseEntity.status(200).body(responseDTO);
    }

    @DeleteMapping("/blacklist/{userId}")
    public ResponseEntity<ResponseDTO<?>> deleteBlackList(@PathVariable String userId) {
        try {
            blackListService.deleteBlackList(userId);
            ResponseDTO<?> responseDTO = ResponseDTO.custom(HttpStatus.NO_CONTENT,
                    "블랙 리스트 제거 성공", Collections.emptyMap());
            return ResponseEntity.status(204).body(responseDTO);
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            if (message.equals("존재하지 않는 분실물 게시판id")) {
                ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
                return ResponseEntity.status(404).body(responseDTO);
            } else if (message.equals("이미 삭제 상태입니다.")) {
                ResponseDTO<?> responseDTO = ResponseDTO.badRequest(e.getMessage());
                return ResponseEntity.status(400).body(responseDTO);
            }
            ResponseDTO<?> responseDTO = ResponseDTO.forbidden(e.getMessage());
            return ResponseEntity.status(403).body(responseDTO);
        }
    }

}
