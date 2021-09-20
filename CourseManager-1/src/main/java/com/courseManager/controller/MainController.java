package com.courseManager.controller;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTML;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.courseManager.entities.User;
import com.courseManager.entities.UserRepository;
import com.courseManager.helper.AuthDetails;
import com.courseManager.helper.Message;
import com.courseManager.jwtToken.jwtutil;

import ch.qos.logback.core.net.SocketConnector.ExceptionHandler;
import io.jsonwebtoken.lang.Arrays;





@Controller
public class MainController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private jwtutil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/token")
	@ResponseBody
	public String GenerateToken(@RequestBody AuthDetails authdetails) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authdetails.getUsername(), authdetails.getPassword()));
			
		} catch (Exception e) {
			System.out.println("Invalid username or password");
			
		}
		return jwtUtil.generateToken(authdetails.getUsername());
		
		
	}
	RestTemplate  rest;

	@PostMapping("/token")
	public ResponseEntity<?> GenerateToken1(@ModelAttribute AuthDetails authdetails,HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authdetails.getUsername(), authdetails.getPassword()));
			String token=jwtUtil.generateToken(authdetails.getUsername());
			RestTemplate restTemplate = new RestTemplate();
			//HttpHeaders headers = new HttpHeade;
			org.springframework.http.HttpHeaders headers=new org.springframework.http.HttpHeaders();
			headers.set("Authorization","Bearer "+token); // optional - in case you auth in headers
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			return restTemplate.exchange("http://localhost:8081/user/home", HttpMethod.GET, entity,String.class);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT).location(URI.create("http://localhost:8081/login")).build();
		}
			

			

		
	}
	
	private HttpEntity HttpEntity(org.springframework.http.HttpHeaders header) {
		// TODO Auto-generated method stub
		return null;
	}

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
