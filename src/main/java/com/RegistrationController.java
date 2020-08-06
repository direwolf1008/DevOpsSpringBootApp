package com;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

	@GetMapping("/register")
	public String greetingForm(Model model) {
		model.addAttribute("register", new Registration());
		return "register";
	}

	@PostMapping("/register")
	public String greetingSubmit(@ModelAttribute Registration greeting, Model model) {
		model.addAttribute("register", greeting);
		return "complete";
	}

}
