package com.example.userservice.controller;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.example.userservice.dto.UserUpdateDto;
import com.example.userservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        userService.signUp(requestDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }
    
    //로그인
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequestDto requestDto) {
    	Map<String,String> token = userService.login(requestDto);
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("Authorization", "Bearer " + token);

        return ResponseEntity.ok().body(token);
    }
    
    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("X-User-Id") String id) {
    	
    	logger.info("id : {}" , id);
        userService.logout(id);
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }
    
    //토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<Map<String , String>> reissueToken(@RequestBody RefreshTokenDto tokenDto)
    {
    	String newAccessToken = userService.reissueToken(tokenDto.getRefreshToken());
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
    
    //회원정보
    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getMyInfo(@RequestHeader("X-User-Id") String id) {
        UserInfoDto userInfo = userService.getUserInfoById(id);
        return ResponseEntity.ok(userInfo);
    }
    
    //회원정보 수정
    @PostMapping("/me")
    public ResponseEntity<UserInfoDto> updateMyInfo(
    		@RequestHeader("X-User-Id") String id , 
    		@Valid @RequestBody UserUpdateDto upadateDto) 
    {
    	logger.info("회원정보 수정 controller id : {}" , id);
    	UserInfoDto userInfo = userService.UpdateUserInfoById(id , upadateDto);
        return ResponseEntity.ok(userInfo);
    }
    
    //중복체크
    @GetMapping("/duplicate")
    public ResponseEntity<Map<String, Boolean>> duplicate(@RequestParam("id") String id)
    {
    	boolean dupleChk = userService.duplicatedIdCheck(id);
        return ResponseEntity.ok(Map.of("success", dupleChk));
    }
    
    //회원탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestHeader("X-User-Id") String id)
    {
    	userService.deleteId(id);
        return ResponseEntity.ok("회원탈퇴처리 되었습니다.");
    }
    
    
}