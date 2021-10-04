package com.courseManager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.courseManager.entities.User;
import com.courseManager.entities.UserRepository;

public class CustomUserDerailsImplement implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user= userRepository.getUserByUsername(username);
		CustomUserDetails custemUser= new CustomUserDetails(user);
		return custemUser;
	}

}
