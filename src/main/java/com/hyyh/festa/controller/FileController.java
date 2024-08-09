package com.hyyh.festa.controller;

import com.hyyh.festa.dto.Presigned;
import com.hyyh.festa.dto.ResponseDTO;
import com.hyyh.festa.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;
    @GetMapping("admin/events/up")
    public ResponseEntity<ResponseDTO<Presigned>> getEventPresignedUrl(){
        String url = fileService.getEventPreSignedUrl();
        Presigned presigned = new Presigned(url);
        return ResponseEntity
                .status(200)
                .body(ResponseDTO.ok("S3 pre signed URL 발급 성공",presigned));
    }
}