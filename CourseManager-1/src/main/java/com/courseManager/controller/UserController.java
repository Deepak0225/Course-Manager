package com.courseManager.controller;

import java.net.http.HttpRequest;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.courseManager.entities.User;
import com.courseManager.entities.UserRepository;
import com.courseManager.entities.coursedetails;
import com.courseManager.entities.coursedetailsRepository;
import com.courseManager.helper.Message;
import com.courseManager.jwtToken.jwtutil;

import org.springframework.security.core.userdetails.UserDetails;


@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private coursedetailsRepository coursedetailsRepository;
	
	@Autowired
	private jwtutil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	
	
	@ModelAttribute
	public void addCommonData(Model model,HttpServletResponse response) {
		String token=response.getHeader("token");
		System.out.println(token);
		if (token!=null) {
			String userName=jwtUtil.extractUsername(token);
			User user=userRepository.getUserByUsername(userName);
			model.addAttribute("user", user);
		}
		
	}
	
	
	@GetMapping("/home")
	public String home(Model model,HttpServletResponse response) {
		String token=response.getHeader("token");
		String userName=jwtUtil.extractUsername(token);
		int length=userRepository.getUserByUsername(userName).getCourses().size();
		model.addAttribute("length", length);

		return "user/home";
	}
	
	@GetMapping("/profile")
	public String profile() {
		return "user/profile";
	}
	
	
	@GetMapping("/add-course")
	public String add_course(Model model) {
		model.addAttribute("course",new coursedetails());
		return "user/add_course";
	}
	
	@PostMapping("/process_course")
	public String processCours(@ModelAttribute coursedetails course,HttpServletResponse response,HttpSession session) {
		try {
			String token=response.getHeader("token");
			String userName=jwtUtil.extractUsername(token);
			User user=userRepository.getUserByUsername(userName);
			course.setUser(user);
			user.getCourses().add(course);
			userRepository.save(user);
			session.setAttribute("message", new Message("Course is added successfully!..", "alert-success"));
			
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong!..", "alert-danger"));
		}
		return "user/add_course";
	}
	
	@GetMapping("/show-course")
	public String showCourse(Model model,HttpServletResponse response){
		String token=response.getHeader("token");
		String userName=jwtUtil.extractUsername(token);
		User user=userRepository.getUserByUsername(userName);
		List<coursedetails> courseList=user.getCourses();
		model.addAttribute("courseList", courseList);
		return "user/show_courses";
	}
	
	@PostMapping("/update-course/{idc}")
	public String updateCourse(@PathVariable("idc") Integer idc,Model model){
		coursedetails course=coursedetailsRepository.getById(idc);
		model.addAttribute("course", course);
		return "user/update_course";
	}
	
	@RequestMapping("/more-info/{idc}")
	public String moreInfo(@PathVariable("idc") Integer idc,Model model,HttpServletResponse response,HttpSession session){
		String token=response.getHeader("token");
		String userName=jwtUtil.extractUsername(token);
		User user=userRepository.getUserByUsername(userName);
		try {
			
			coursedetails course=coursedetailsRepository.getById(idc);
			model.addAttribute("course", course);
			if (course.getUser().getId()!=user.getId()) {
				session.setAttribute("message", new Message("You are not authorized to see this course!..", "alert-danger"));
				return "user/show_courses";
			}
			else {
				return "user/more_info";
			}
		} catch (Exception e) {
			session.setAttribute("message", new Message("You are not authorized to see this course!..", "alert-danger"));
			List<coursedetails> courseList=user.getCourses();
			model.addAttribute("courseList", courseList);
			return "user/show_courses";
		}
		
	}
	
	@PostMapping("/process_update")
	public String processUpdate(@ModelAttribute coursedetails course,HttpServletResponse response,Model model,HttpSession session) {
		
		try {
			coursedetails oldCourse=coursedetailsRepository.getById(course.getIdc());
			String token=response.getHeader("token");
			String userName=jwtUtil.extractUsername(token);
			User user=userRepository.getUserByUsername(userName);
			course.setUser(user);
			coursedetailsRepository.save(course);
			List<coursedetails> courseList=user.getCourses();
			model.addAttribute("courseList", courseList);
			session.setAttribute("message", new Message("Your course is updated successfully!..", "alert-success"));
			return "user/show_courses";
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong!..", "alert-danger"));
			return "user/update_course";
			
		}
	}
	@RequestMapping("/delete-course/{idc}")
	public String deleteCourse(@PathVariable("idc") Integer idc,HttpSession session,HttpServletResponse response,Model model) {
		String token=response.getHeader("token");
		String userName=jwtUtil.extractUsername(token);
		User user=userRepository.getUserByUsername(userName);
		try {
			coursedetails course=coursedetailsRepository.getById(idc);
			if (course.getUser().getId()==user.getId()) {
				user.getCourses().remove(course);
				userRepository.save(user);
				session.setAttribute("message", new Message("Course is deleted successsfully!..", "alert-success"));
				
				
			}
			else {
				session.setAttribute("message", new Message("You are not authorized!..", "alert-danger"));
				
			}
		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong!..", "alert-danger"));
			
			
		}
		List<coursedetails> courseList=user.getCourses();
		model.addAttribute("courseList", courseList);
		return "user/show_courses";
		
		
	}
}
