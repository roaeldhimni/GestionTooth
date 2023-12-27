package com.eldhimni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eldhimni.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {


	public User findByEmail(String email);

	public void save(String username);

}