package com.example.userservice.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findById(String id);
	void deleteById(String id);
	boolean existsById(String id);
}