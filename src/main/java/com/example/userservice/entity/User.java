package com.example.userservice.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false) // unique = true 추가
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    @Embedded
    private Address address;
}