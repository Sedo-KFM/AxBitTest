package com.sedo.AxBitTest.controllers;

import ch.qos.logback.classic.Logger;
import com.sedo.AxBitTest.repo.AuthorRepository;
import com.sedo.AxBitTest.repo.BookRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	static private final Logger logger = (Logger) LoggerFactory.getLogger(MainController.class);

	@GetMapping("")
	public String home(Model model) {
		logger.trace("GET /");
		return "home";
	}
}
