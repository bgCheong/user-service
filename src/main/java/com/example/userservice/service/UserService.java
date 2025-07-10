package com.example.userservice.service;


import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.userservice.dto.LoginRequestDto;
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.dto.UserInfoDto;
import com.example.userservice.entity.Address;
import com.example.userservice.entity.User;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValiditySeconds;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String UserGroupKey = "users:idList"; 

    @Transactional
    public void signUp(SignUpRequestDto requestDto) {
    	logger.info("request_data : {}" , requestDto);
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = new User();
        user.setId(requestDto.getId());
        user.setEmail(requestDto.getEmail());
        user.setPassword(encodedPassword);
        user.setName(requestDto.getName());
        user.setPhoneNumber(requestDto.getPhoneNumber());
        Address address = new Address();
        address.setZipcode(requestDto.getZipcode());
        address.setStreetAddress(requestDto.getStreetAddress());
        address.setDetailAddress(requestDto.getDetailAddress());
        user.setAddress(address);
        userRepository.save(user);
        redisTemplate.opsForSet().add(UserGroupKey, requestDto.getId());
    }
    
    public boolean duplicatedIdCheck(String id)
    {
    	return redisTemplate.opsForSet().isMember(UserGroupKey , id);
    }
    
    @Transactional
    public Map<String,String> login(LoginRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 및 비밀번호가 일치하지 않습니다.");
        }
        
        String accessToken = jwtUtil.createAccessToken(requestDto.getId());
        String refreshToken = jwtUtil.createRefreshToken(requestDto.getId());
        
        redisTemplate.opsForValue().set(requestDto.getId(), refreshToken, Duration.ofSeconds(refreshTokenValiditySeconds));
        
        Map<String ,String> token = new HashMap<>();
        
        token.put("accessToken", accessToken);
        token.put("refreshToken" , refreshToken);

        return token;
    }
    
    
    
    public String reissueToken(String token)
    {
    	logger.info("refresh token 진입");
    	if(!jwtUtil.validateToken(token))
    	{
    		throw new IllegalArgumentException("유효한 토큰이 아닙니다.");
    	}
    	
    	String id = jwtUtil.getIdFromToken(token);
    	
    	logger.info("대상 id : {}" , id);
    	
    	String storedRefreshToken = redisTemplate.opsForValue().get(id);
        if (!token.equals(storedRefreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 일치하지 않습니다.");
        }
    	
    	return jwtUtil.createAccessToken(id);
    }
    
    public void logout(@RequestHeader("X-User-Id") String id)
    {
    	logger.info("로그아웃 대상 id : {}" , id);
    	
    	redisTemplate.delete(id);
    }
    
    public UserInfoDto getUserInfoById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserInfoDto(user);
    }
    
    @Transactional
    public void deleteId(@RequestHeader("X-User-Id") String id)
    {
    	if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("ID '" + id + "'에 해당하는 사용자를 찾을 수 없습니다.");
        }
        
        userRepository.deleteById(id);
        
        try {
            redisTemplate.opsForSet().remove(UserGroupKey, id);
        } catch (DataAccessException e) {
        	logger.info("로그아웃 대상 id : {}" , id);
        	logger.info("Redis에서 ID '{}' 삭제 중 오류가 발생했습니다. 하지만 DB에서는 삭제되었습니다." , id);
        }
    }
}