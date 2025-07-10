package com.example.userservice.controller;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.LoginRequestDto;
import com.example.userservice.dto.RefreshTokenDto;
import com.example.userservice.dto.SignUpRequestDto;
import com.example.userservice.dto.UserInfoDto;
import com.example.userservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequestDto requestDto) {
    	Map<String,String> token = userService.login(requestDto);
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer " + token);

        return ResponseEntity.ok().body(token);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("X-User-Id") String id) {
    	
    	logger.info("id : {}" , id);
        userService.logout(id);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<Map<String , String>> reissueToken(@RequestBody RefreshTokenDto tokenDto)
    {
    	String newAccessToken = userService.reissueToken(tokenDto.getRefreshToken());
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
    
    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getMyInfo(@RequestHeader("X-User-Id") String id) {
        UserInfoDto userInfo = userService.getUserInfoById(id);
        return ResponseEntity.ok(userInfo);
    }
    
    @PostMapping("/duplicate")
    public ResponseEntity<Map<String, Boolean>> duplicate(@RequestParam("id") String id)
    {
    	boolean dupleChk = userService.duplicatedIdCheck(id);
        return ResponseEntity.ok(Map.of("success", dupleChk));
    }
    
    
}