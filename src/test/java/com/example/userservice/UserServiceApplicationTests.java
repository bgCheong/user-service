package com.example.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import com.example.userservice.jwt.JwtUtil;

@SpringBootTest
class UserServiceApplicationTests {

	@MockBean
	private RedisTemplate<String, String> redisTemplate;
	
	@MockBean
	private JwtUtil jwtUtil; 
	
	@Test
	void contextLoads() {
	}

}
