package com.sedo.AxBitTest.controllers;

import com.sedo.AxBitTest.models.Book;
import com.sedo.AxBitTest.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/books")
	public String books(Model model) {
		Iterable<Book> books = bookRepository.findAll();
		model.addAttribute("books", books);
		return "books";
	}

	@GetMapping("/books/add")
	public String booksAdd(Model model) {
		return "books-add";
	}

	@PostMapping("/books/add")
	public String booksAddPost(@RequestParam String isbn, Model model) {
		long parsedIsbn = Long.parseLong(isbn);
		if (Long.toString(parsedIsbn).length() != 13) {
			return "error";
		}
		Book book = new Book(parsedIsbn);
		bookRepository.save(book);
		return "redirect:/books";
	}
}
