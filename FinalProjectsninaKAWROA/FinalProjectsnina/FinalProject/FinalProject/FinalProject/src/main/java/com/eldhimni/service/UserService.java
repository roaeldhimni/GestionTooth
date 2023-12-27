package com.eldhimni.service;

import com.eldhimni.entity.User;

public interface UserService {

	public User saveUser(User user);

	public void removeSessionMessage();

}