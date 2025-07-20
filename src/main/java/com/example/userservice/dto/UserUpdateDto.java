package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @NotBlank
    private String email;
    @NotBlank
    @NotBlank
    private String name;
    private String phoneNumber;
    private String zipcode;
    private String streetAddress;
    private String detailAddress;
    
}
