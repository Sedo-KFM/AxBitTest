package com.sedo.AxBitTest.controllers;

import com.sedo.AxBitTest.models.Author;
import com.sedo.AxBitTest.models.Book;
import com.sedo.AxBitTest.repo.AuthorRepository;
import com.sedo.AxBitTest.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class MainController {

	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/")
	public String home(Model model) {
		return "home";
	}

	@GetMapping("/test")
	public String test(Model model) {
		Iterable<Author> authors = authorRepository.findAll();
		Author firstAuthor = authors.iterator().next();
		Iterable<Book> books = bookRepository.findAll();
		Book firstBook = books.iterator().next();
		firstAuthor.getBooks().add(firstBook);
		authorRepository.save(firstAuthor);
		model.addAttribute("answer", 0);
		return "authors";
	}
}
