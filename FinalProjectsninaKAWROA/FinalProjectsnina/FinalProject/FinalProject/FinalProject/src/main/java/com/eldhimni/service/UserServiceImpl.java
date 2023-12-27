package com.eldhimni.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eldhimni.entity.User;
import com.eldhimni.repository.UserRepo;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public User saveUser(User user) {
	    String password = passwordEncoder.encode(user.getPassword());

	    // Vérifier si le rôle est déjà défini
	    if (user.getRole() == null) {
	        user.setRole("ROLE_USER"); // Définir le rôle par défaut uniquement s'il n'est pas déjà défini
	    }

	    user.setPassword(password);
	    User newuser = userRepo.save(user);
	    return newuser;
	}



	@Override
	public void removeSessionMessage() {

		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg");
	}

}