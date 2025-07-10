package com.example.userservice.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {
    @NotBlank
    private String id;
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "비밀번호는 최소 8자 이상, 대문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
    private String password;
    @NotBlank
    private String name;
    private String phoneNumber;
    private String zipcode;
    private String streetAddress;
    private String detailAddress;
}