package com.hyyh.festa.handler;

import com.hyyh.festa.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<?>> handleValidationExceptions(MethodArgumentNotValidException e) {
        ResponseDTO<?> responseDTO = ResponseDTO.badRequest("요청 양식 불량");
        return ResponseEntity.status(400).body(responseDTO);
    }
}
