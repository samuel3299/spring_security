package com.security.springdata.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * controller layer
 */
@RestController
@RequestMapping("/home")
public class WlcomeHome {

	/**
	 * Returns to home page
	 * @return returns a String
	 */
	@GetMapping
	public String welcomeHome() {
		return "welcome Home!";
	}
}
