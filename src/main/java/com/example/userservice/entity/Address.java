package com.example.userservice.entity;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Address {
    private String zipcode;
    private String streetAddress;
    private String detailAddress;
}