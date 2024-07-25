package com.hyyh.festa.controller;

import com.hyyh.festa.dto.PostDTO;
import com.hyyh.festa.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    //[POST] 분실물 게시글 등록 api
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDTO){

        return ResponseEntity.ok().body("TEST"); //구현시, ResponseDTO 객체가 들어가야함

    }

    //[GET] 분실물 게시글 리스트 조회 api
    @GetMapping
    public ResponseEntity<?> getPosts(
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0")int page){

        return ResponseEntity.ok().body("TEST");
    }

    //[GET] 분실물 게시글 단일 조회 api
    @GetMapping("/{postId}")
    public ResponseEntity<?> getOnePost(@PathVariable long postId){

        return ResponseEntity.ok().body("TEST");
    }

    //[DELETE] 분실물 게시글 삭제 api
    @DeleteMapping("/{postId}") //구현 방법 다시 생각해봐야 할 것 같다.
    public ResponseEntity<?> deleteOnePost(@PathVariable long postId){

        return ResponseEntity.ok().body("TEST");
    }

    //[POST] 분실물 사진 등록 api (프론트와 상의 후 개발)




}
