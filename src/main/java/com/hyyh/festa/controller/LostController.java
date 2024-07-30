package com.hyyh.festa.controller;

import com.hyyh.festa.dto.LostDTO;
import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.service.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.hyyh.festa.dto.*;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/losts")
public class LostController {

    private final LostService lostService;

    //[POST] 분실물 게시글 등록 api
    @PostMapping
    public ResponseEntity<?> createLost(@RequestBody LostDTO lostDTO){

        return ResponseEntity.ok().body("TEST"); //구현시, ResponseDTO 객체가 들어가야함

    }

    //[GET] 분실물 게시글 리스트 조회 api
    @GetMapping
    public ResponseEntity<?> getLosts(
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0")int page){

        return ResponseEntity.ok().body("TEST");
    }

    //[GET] 분실물 게시글 단일 조회 api
    @GetMapping("/{lostId}")
    public ResponseEntity<ResponseDTO<?>> getOneLost(@PathVariable long lostId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 존재하고, 사용자가 인증된 상태이며, principal이 UserDetails의 인스턴스인 경우
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            // 사용자가 ADMIN 권한을 가진 경우
            if (userDetails.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ADMIN"))) {
                try {
                    Optional<GetAdminLostDTO> response = lostService.getOneAdminLost(lostId);
                    if (response.isEmpty()) {
                        return ResponseEntity.status(404).body(ResponseDTO.notFound("존재하지 않는 게시글입니다."));
                    }
                    else {
                        return ResponseEntity.status(200).body(ResponseDTO.ok("분실물 게시글 단건 조회 성공", response));
                    }
                } catch (Exception e){
                    return ResponseEntity.status(500).body(ResponseDTO.internalServerError("서버 내부 에러"));
                }
        }
        // USER 권한이거나 토큰이 없는 경우 처리
            try {
                Optional<GetUserLostDTO> response = lostService.getOneUserLost(lostId);
                if (response.isEmpty()) {
                    return ResponseEntity.status(404).body(ResponseDTO.notFound("존재하지 않는 게시글입니다."));
                }
                else {
                    return ResponseEntity.status(200).body(ResponseDTO.ok("분실물 게시글 단건 조회 성공", response));
                }
            } catch (Exception e){
                return ResponseEntity.status(500).body(ResponseDTO.internalServerError("서버 내부 에러"));
            }
        }
    return ResponseEntity.status(403).body(ResponseDTO.forbidden("접근 제한"));
    }

    //[DELETE] 분실물 게시글 삭제 api
    @DeleteMapping("/{lostId}") //구현 방법 다시 생각해봐야 할 것 같다.
    public ResponseEntity<?> deleteOneLost(@PathVariable long lostId){

        return ResponseEntity.ok().body("TEST");
    }




}
