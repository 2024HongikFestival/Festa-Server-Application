package com.hyyh.festa.controller;

import com.hyyh.festa.dto.GetAdminLostDTO;
import com.hyyh.festa.dto.LostPageResponse;
import com.hyyh.festa.dto.LostRequestDTO;
import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.service.LostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/losts")
public class LostController {

    private final LostService lostService;

    @PostMapping
    public ResponseEntity<ResponseDTO<?>> createLost(@AuthenticationPrincipal UserDetails userDetails,
                                                     @Valid @RequestBody LostRequestDTO lostRequestDTO) {
        try {
            GetAdminLostDTO createdLost = lostService.createLost(userDetails, lostRequestDTO);
            ResponseDTO<?> responseDTO = ResponseDTO.created("분실물 게시글 생성 성공", createdLost);
            return ResponseEntity.status(201).body(responseDTO);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("존재하지 않는 사용자id")) {
                ResponseDTO<?> responseDTO = ResponseDTO.notFound(e.getMessage());
                return ResponseEntity.status(404).body(responseDTO);
            }
            ResponseDTO<?> responseDTO = ResponseDTO.forbidden(e.getMessage());
            return ResponseEntity.status(403).body(responseDTO);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<?>> getListLost(
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String userId,
            @AuthenticationPrincipal UserDetails userDetails){
        try {
            List<?> losts;
            int totalPage;

            if (userDetails != null && getAuthority(userDetails).equals("ADMIN")){
                losts = lostService.getLostListByAdmin(page-1, date, userId);
                totalPage = lostService.countTotalPage(date);
            } else {
                losts = lostService.getLostListByUser(page-1, date);
                totalPage = lostService.countPublishedTotalPage(date);
            }

            if (losts.isEmpty()){
                return ResponseEntity.status(404).body(ResponseDTO.notFound("분실물 게시글이 존재하지 않습니다."));
            }

            return ResponseEntity.ok(ResponseDTO.ok("분실물 목록 조회 성공", new LostPageResponse(page, totalPage, losts)));
        } catch (Exception e){
            return ResponseEntity.status(500).body(ResponseDTO.internalServerError("서버 내부 에러"+e));
        }
    }

    @GetMapping("/{lostId}")
    public ResponseEntity<ResponseDTO<?>> getOneLost(@PathVariable Long lostId,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Optional<?> response;
            if (userDetails != null && getAuthority(userDetails).equals("ADMIN")) {
                response = lostService.getOneAdminLost(lostId);
            } else {
                response = lostService.getOneUserLost(lostId);
            }

            if (response.isPresent()) {
                return ResponseEntity.ok(ResponseDTO.ok("분실물 게시글 단건 조회 성공", response.get()));
            } else {
                return ResponseEntity.status(404).body(ResponseDTO.notFound("존재하지 않는 게시글입니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseDTO.internalServerError("서버 내부 에러"+e));
        }
    }

    /*userDetails로부터 자격증명 가져오는 메서드*/
    private String getAuthority(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
    }
}