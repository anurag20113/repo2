package com.smart.Controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.smart.dao.UserRepository;
import com.smart.entites.User;
import com.smart.helper.Message;

@Controller

public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home- Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping(value="/about", method = RequestMethod.GET)
	public String about(Model model) {
		model.addAttribute("about", "Home- Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("about", "Register- Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("about", "Login- Smart Contact Manager");
		return "login";
	}
	
	//handling the registation page
	@RequestMapping(value = "/do_register",method = RequestMethod.POST)
	public String registerUser
	          (@ModelAttribute("user")User user,
              @RequestParam(value = "agreement",defaultValue = "false") 
	                        boolean agreement,Model model,HttpSession session) {
		
		try {
			if(!agreement) {
				System.out.println("You Have not Agree Terms And condition");
				throw new Exception("You Have not Agree Terms And condition");
			}
			
			user.setRole("Role_USER");
			user.setEnabled(true);
			user.setImgurl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement"+agreement);
			System.out.println("User"+user);
			
			User result=this.userRepository.save(user);
			model.addAttribute("user",new User());
			session.setAttribute("Message", new Message("Successfully Registerd!!","alert-success"));
			
			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("Message", new Message("Something Went wrong"+e.getMessage(),"alert-danger"));
		
			return "signup";
		}
		
		
		  
	}

    //handller for custom login
	@GetMapping("/signin")
	public String CustomLogin(Model model) {
		
		model.addAttribute("title","Login page");
		return "signin";
	}
	
}
