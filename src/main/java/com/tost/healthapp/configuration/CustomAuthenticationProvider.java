package com.tost.healthapp.configuration;

import com.tost.healthapp.domain.User;
import com.tost.healthapp.exceptions.UserNotFoundException;
import com.tost.healthapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.security.core.userdetails.User;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final UserService userService;

	public CustomAuthenticationProvider(UserService userService) {
        super();
		this.userService = userService;
	}
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String username = authentication.getName();
		final String password = authentication.getCredentials().toString();

		User user = null;
		try {
			user = userService.doesUserExist(username);
		} catch (UserNotFoundException e) {
		}

		if (user == null || !user.getEmail().equalsIgnoreCase(username)) {
			throw new BadCredentialsException("Username not found.");
		}

		if (!password.equals(user.getPassword())) {
			throw new BadCredentialsException("Wrong password.");
		}
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if(user.getRole() == 1) {
			authorities.add(new SimpleGrantedAuthority("ROLE_DOCTOR"));
		} else {
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		}		
        final UserDetails principal = new org.springframework.security.core.userdetails.User(username, password, authorities);        
		return new UsernamePasswordAuthenticationToken(principal, password, authorities);
	}
	
	@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
          UsernamePasswordAuthenticationToken.class);
    }

}