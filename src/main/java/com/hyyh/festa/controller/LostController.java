package com.hyyh.festa.controller;

import com.hyyh.festa.dto.LostDTO;
import com.hyyh.festa.service.LostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/losts")
public class LostController {

    @Autowired
    private LostService lostService;

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
    public ResponseEntity<?> getOneLost(@PathVariable long lostId){

        return ResponseEntity.ok().body("TEST");
    }

    //[DELETE] 분실물 게시글 삭제 api
    @DeleteMapping("/{lostId}") //구현 방법 다시 생각해봐야 할 것 같다.
    public ResponseEntity<?> deleteOneLost(@PathVariable long lostId){

        return ResponseEntity.ok().body("TEST");
    }




}
