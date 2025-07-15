package com.example.userservice.dto;

import com.example.userservice.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String email;
    private String name;
    private String phoneNumber;
    private String zipcode;
    private String streetAddress;
    private String detailAddress;
    
    
    public UserInfoDto(User user)
    {
    	this.email = user.getEmail();
    	this.name = user.getName();
    	this.phoneNumber = user.getPhoneNumber();
    	this.zipcode = user.getAddress().getZipcode();
    	this.detailAddress = user.getAddress().getDetailAddress();
    	this.streetAddress = user.getAddress().getStreetAddress();
    }


}