package com.courseManager.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.courseManager.emailService.EmailService;
import com.courseManager.entities.User;
import com.courseManager.entities.UserRepository;
import com.courseManager.helper.Message;

@Controller
public class ForgotController {
	
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private EmailService emailSevice;
	
	Random random=new Random(1000);
	
	@GetMapping("/forgot")
	public String forgotPassword() {
		return "forgotPassword/forgot_password";
	}
	@PostMapping("/sent-otp")
	public String verification(@RequestParam("username") String username, HttpSession session) {
		try {
			//Generate random OTP ....
			int otp=random.nextInt(9999999);
			String username1=username;
			//Message ....
			String message="your OTP to reset password of Course Manager account with username as "
							+"<h1"
							+username1
							+"</h1>"
							+" is "
							+"<h1>"
							+"<strong>"
							+otp
							+"</strong>"
							+"</h1>"
							+".";
			
			// Subject for email
			String subject="OTP from course manager to reset password";
			
			// to whom sent the email
			User user= userrepository.getUserByUsername(username);
			String email_id=user.getEmail();
			
			//sent mail
			emailSevice.sendEmain(subject, message, email_id);
			
			
			session.setAttribute("myotp", otp);
			session.setAttribute("username", username1);
			return "forgotPassword/verify_otp";
			
		} catch (Exception e) {
			session.setAttribute("message", new Message("Username is not registered !", "alert-danger"));
			return "forgotPassword/forgot_password";
		}
	
	}
	//verift otp
	
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int OTP,HttpSession session) {
		
		int myotp=(int)session.getAttribute("myotp");
		
		if (myotp==OTP) {
			session.setAttribute("username", session.getAttribute("username"));
			return "forgotPassword/changePassword";
		}
		else {
			session.setAttribute("message", new Message("Please check entered otp", "alert-danger"));
			return "forgotPassword/verify_otp";
		}
	}
	@PostMapping("/change-Password")
	public String changePassword(@RequestParam("password") String password,HttpSession session) {
		
		String username=(String)session.getAttribute("username");
		BCryptPasswordEncoder encodepassword= new BCryptPasswordEncoder();
		String passwordEncode= encodepassword.encode(password);
		User user= userrepository.getUserByUsername(username);
		user.setPassword(passwordEncode);
		userrepository.save(user);
		System.out.println(password);
		System.out.println(encodepassword.encode("12345"));
		System.out.println(user);
		session.setAttribute("message", new Message("your password has been changed successfully!!..", "alert-success"));
		return "login";
	}
}
