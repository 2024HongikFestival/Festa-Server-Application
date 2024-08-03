package com.hyyh.festa.controller;

import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.service.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/losts")
public class LostController {

    private final LostService lostService;

    @GetMapping
    public ResponseEntity<ResponseDTO<?>> getListLost(
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @AuthenticationPrincipal UserDetails userDetails){
        try {
            Page<?> response;
            if (userDetails != null && getAuthority(userDetails).equals("ADMIN")){
                response = lostService.getListAdminLost(page, date);
            } else {
                response = lostService.getListUserLost(page, date);
            }

            if (response.isEmpty()){
                return ResponseEntity.status(404).body(ResponseDTO.notFound("분실물 게시글이 존재하지 않습니다."));
            }
            return ResponseEntity.ok(ResponseDTO.ok("분실물 목록 조회 성공"+ date, response));
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