package com.tost.healthapp.controllers;

import com.tost.healthapp.domain.User;
import com.tost.healthapp.exceptions.UserNotFoundException;
import com.tost.healthapp.helpers.ExecutionStatus;
import com.tost.healthapp.services.DoctorService;
import com.tost.healthapp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/account/*")
public class UserAccountController {
	
	final static Logger logger = LoggerFactory.getLogger(UserAccountController.class);
	
	UserService userService;	
	DoctorService docService;
	AuthenticationProvider authenticationProvider;
	
	@Autowired
	public UserAccountController(UserService userService, DoctorService docService, AuthenticationProvider provider) {
		this.userService = userService;
		this.docService = docService;
		this.authenticationProvider = provider;
	}
	
	@GetMapping(value="/token")
	public Map<String, String> getToken(HttpSession session) {
		return Collections.singletonMap("token", session.getId());
	}


	/*
	@PostMapping(value="/login")
	public ExecutionStatus processLogin(@RequestBody User reqUser, HttpServletRequest request)
	{
		Authentication authentication = null;
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(reqUser.getEmail(), reqUser.getPassword());
		try {
			//
			// Delegate authentication check to a custom Authentication provider
			//
			authentication = this.authenticationProvider.authenticate((token));
			//
			// Store the authentication object in the SecurityContextHolder
			SecurityContextHolder.getContext().setAuthentication(authentication);
			//User user = (User) authentication.getPrincipal();
			//user.setPassword(null);
			reqUser.setPassword(null);
			return new ExecutionStatus("USER_LOIGN_SUCCESSFUL", "Login Successful!", reqUser);
		} catch(BadCredentialsException e) {
			return new ExecutionStatus("USER_LOGIN_UNSUCCESSFUL", "Username or password is incorrect. Please try again!");
		}
	}*/

	@PostMapping(value="/signup")
	public ExecutionStatus processSignup(ModelMap model, @RequestBody User reqUser) {
		User user = null;
		try {
			user = userService.doesUserExist(reqUser.getEmail());
		} catch (UserNotFoundException e) {
		}
		if(user != null) {
			return new ExecutionStatus("USER_ACCOUNT_EXISTS", "User account with same email address exists. Please try again!");
		}
		user = new User();
		user.setEmail(reqUser.getEmail());
		user.setPassword(reqUser.getPassword());
		user.setFirstName(reqUser.getFirstname());
		user.setLastname(reqUser.getLastname());
		user.setContactNumber(reqUser.getContactNumber());
		user.setAlternateContactNumber(reqUser.getAlternateContactNumber());
		user.setCityCode(reqUser.getCityCode());
		user.setStateCode(reqUser.getStateCode());
		user.setCountryCode(reqUser.getCountryCode());
		user.setAge(reqUser.getAge());
		user.setGender(reqUser.getGender());
		user.setRole(reqUser.getRole());
		User persistedUser = userService.save(user);
		//
		// Add entry in doctor table if user is a doctor
		//
		docService.addDoctor(user);
		
		return new ExecutionStatus("USER_ACCOUNT_CREATED", "User account successfully created");
	}
	
	@PostMapping(value="/user/update")
	public ExecutionStatus updateUser(ModelMap model, @RequestBody User reqUser) {
		User user = new User();
		user.setId(reqUser.getId());
		user.setFirstName(reqUser.getFirstname());
		user.setLastname(reqUser.getLastname());
		user.setContactNumber(reqUser.getContactNumber());
		user.setAlternateContactNumber(reqUser.getAlternateContactNumber());
		user.setCityCode(reqUser.getCityCode());
		user.setStateCode(reqUser.getStateCode());
		user.setCountryCode(reqUser.getCountryCode());
		user.setAge(reqUser.getAge());
		user.setGender(reqUser.getGender());
		userService.update(user);
		return new ExecutionStatus("USER_ACCOUNT_UPDATED", "User account successfully updated");
	}

	@PostMapping(value="/update", produces="application/json")
	public ModelAndView updateProfile(ModelMap model, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("address") String address, 
			@RequestParam("contact_number") String contactNumber) {
		return new ModelAndView("update", model);
	}
	
	@PostMapping(value="/forgotpassword/process", produces="application/json")
	public ModelAndView processForgotPassword(ModelMap model, @RequestParam("emailaddress") String email) {
		
		User user = null;
		try {
			user = userService.doesUserExist(email);
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user != null) {
			
		}	
		model.addAttribute("message", "An email notification is sent to the registered email address.");
		return new ModelAndView("forgotpassword", model);
	}
	@RequestMapping("signup/process")
	public String processSignup(ModelMap model,
								@RequestParam("nickname") String nickname,
								@RequestParam("emailaddress") String emailAddress,
								@RequestParam("password") String password) {
		model.addAttribute("login", true);
		model.addAttribute("nickname", nickname);
		return "index";
	}
}
