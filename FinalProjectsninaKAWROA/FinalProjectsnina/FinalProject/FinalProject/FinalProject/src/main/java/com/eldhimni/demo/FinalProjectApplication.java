package com.eldhimni.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.eldhimni.entity.User;
import com.eldhimni.repository.UserRepo;
import com.eldhimni.service.UserService;
import com.eldhimni.service.UserServiceImpl;

@SpringBootApplication
@EntityScan (basePackages = "com.eldhimni.entity")
@ComponentScan (basePackages = {"com.eldhimni.controller","com.eldhimni.service","com.eldhimni.config"})
@EnableJpaRepositories(basePackages = "com.eldhimni.repository")
public class FinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}


}
