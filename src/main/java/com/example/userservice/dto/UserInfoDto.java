package com.example.userservice.dto;

import com.example.userservice.entity.User;
import lombok.Getter;

@Getter
public class UserInfoDto {
    private String email;
    private String name;
    private String phoneNumber;

    public UserInfoDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
    }
}