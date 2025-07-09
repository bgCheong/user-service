package com.example.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException이 발생했을 때 이 메서드가 실행됨
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        // 에러 메시지를 포함한 Map 생성
        Map<String, String> errorResponse = Map.of("error", ex.getMessage());

        // 400 Bad Request 상태 코드와 함께 에러 메시지를 JSON 형태로 반환
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}