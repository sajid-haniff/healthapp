package com.tost.healthapp.services;

import com.tost.healthapp.domain.User;
import com.tost.healthapp.exceptions.UnmatchingUserCredentialsException;
import com.tost.healthapp.exceptions.UserNotFoundException;

public interface UserService {
	
	User save(User user);
	
	void update(User user);
	
	User doesUserExist(String email) throws UserNotFoundException;
	
	User getByEmail(String email) throws UserNotFoundException;
	
	User isValidUser(String email, String password) throws UnmatchingUserCredentialsException;
}
