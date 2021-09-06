package com.courseManager.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.courseManager.entities.User;
import com.courseManager.entities.UserRepository;
import com.courseManager.helper.Message;


@Controller
public class MainController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/home")
	public String homepage() {
		return "home";
	}
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	@GetMapping("/sign_up")
	public String signup(Model model) {
		model.addAttribute("user", new User());
		return "signUp";
	}
	
	@PostMapping("/process_user")
	public String processUser(@ModelAttribute("user") User user,HttpSession session){
		
		try {
			String password=user.getPassword();
			BCryptPasswordEncoder encodepassword= new BCryptPasswordEncoder();
			String passwordEncode= encodepassword.encode(password);
			user.setPassword(passwordEncode);
			userRepository.save(user);
			session.setAttribute("message", new Message("successfully registered!!..", "alert-success"));
			return "login";
			
		} catch (Exception e) {
			session.setAttribute("message", new Message("This username is already taken !..", "alert-danger"));
			return "signUp";
		}
		
		
	}

}
