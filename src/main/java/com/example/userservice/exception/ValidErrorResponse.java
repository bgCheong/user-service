package com.example.userservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ValidErrorResponse {
	
	private final String field;
    private final String message;
}
