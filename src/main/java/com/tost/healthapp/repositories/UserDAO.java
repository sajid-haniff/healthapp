package com.tost.healthapp.repositories;

import java.util.List;

import com.tost.healthapp.domain.User;

public interface UserDAO  {
	
	User save(User user);
	
	List<User> findByEmail(String email);
	
	List<User> findByEmailAndPassword(String email, String password);
	
	void update(User user);
	
}
