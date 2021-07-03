package com.sedo.AxBitTest.controllers;

import com.sedo.AxBitTest.models.Author;
import com.sedo.AxBitTest.repo.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

	@Autowired
	private AuthorRepository authorRepository;

	@GetMapping("/")
	public String greeting(Model model) {
		Iterable<Author> authors = authorRepository.findAll();
		model.addAttribute("authors", authors);
		return "home";
	}

}
