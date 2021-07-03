package com.sedo.AxBitTest.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

	@GetMapping("/")
	public String greeting(@RequestParam(name="request", required=false, defaultValue="Request something") String request, Model model) {
		model.addAttribute("answer", request);
		return "simpleAnswer";
	}

}
