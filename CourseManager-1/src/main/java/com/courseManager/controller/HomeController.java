package com.courseManager.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.courseManager.emailService.EmailService;
import com.courseManager.entities.User;
import com.courseManager.entities.UserRepository;
import com.courseManager.helper.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.courseManager.service.logindetails;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/login_process")
	public String login_process(@ModelAttribute logindetails logindetails,HttpServletRequest request,HttpServletResponse response){
		RestTemplate restTemplate = new RestTemplate();

		// According OAuth documentation we need to send the client id and secret key in the header for authentication
		String credentials = "courseManager:secret";
		String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Basic " + encodedCredentials);

		HttpEntity<String> requestok = new HttpEntity<String>(headers);
		String access_token_url = "http://localhost:8081/oauth/token";
		access_token_url += "?username="+logindetails.getUsername();
		access_token_url += "&password="+logindetails.getPassword();
		access_token_url += "&grant_type=password";
		access_token_url += "&redirect_uri=http://localhost:8081/user/private";
		String access_token=restTemplate.exchange(access_token_url,HttpMethod.POST, requestok, HashMap.class).getBody().get("access_token").toString();
		

		request.setAttribute("access_token", access_token);
		response.addHeader("access_token", access_token);
		response.setHeader("access_token", access_token);
		String url="http://localhost:8081/user/private?access_token="+access_token;
		return "redirect:"+url;

	}
	@GetMapping("/login")
	public String login(Model model){
		model.addAttribute("logindetails", new logindetails());
		return "login";
	}
	@GetMapping("/home")
	public String homepage() {
		return "home";
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
