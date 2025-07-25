package com.example.userservice.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ValidErrorResponse> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidErrorResponse(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
    

}