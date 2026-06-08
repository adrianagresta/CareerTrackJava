package com.agresta.CareerTrack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String login() {
		return "login"; // This looks for src/main/resources/templates/login.html
	}
}
