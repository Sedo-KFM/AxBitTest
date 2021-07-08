package com.sedo.AxBitTest.controllers;

import com.sedo.AxBitTest.exceptions.InputDataParseException;
import com.sedo.AxBitTest.models.Author;
import com.sedo.AxBitTest.models.Book;
import com.sedo.AxBitTest.models.Genre;
import com.sedo.AxBitTest.repo.AuthorRepository;
import com.sedo.AxBitTest.repo.BookRepository;
import com.sedo.AxBitTest.repo.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Controller
public class BookController {

	private String message;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private GenreRepository genreRepository;

	@GetMapping("/books")
	public String books(Model model) {
		Iterable<Book> books = bookRepository.findAll();
		model.addAttribute("books", books);
		return "books";
	}

	@GetMapping("/books/add")
	public String booksAdd(Model model) {
		if (this.message != null) {
			model.addAttribute("message", message);
			this.message = null;
		}
		return "books-add";
	}

	@PostMapping("/books/add")
	public String booksAddPost(@RequestParam String isbn, Model model) {
		long parsedIsbn;
		try {
			parsedIsbn = Long.parseLong(isbn);
		} catch (NumberFormatException numberFormatException) {
			throw new InputDataParseException("/books/add", "ISBN must be consist of numbers only");
		}
		if (Long.toString(parsedIsbn).length() != 13) {
			throw new InputDataParseException("/books/add", "ISBN must be consist of 13 numbers");
		}
		Book book = new Book(parsedIsbn);
		bookRepository.save(book);
		return "redirect:/books";
	}

	@GetMapping("/books/{id}")
	public String book(@PathVariable(value = "id") long id, Model model) {
		if (!bookRepository.existsById(id)) {
			return "redirect:/books";
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			model.addAttribute("book", book.get());
			return "book";
		}
		return "redirect:/books";
	}

	@GetMapping("/books/{id}/edit")
	public String bookEdit(@PathVariable(value = "id") long id, Model model) {
		if (!bookRepository.existsById(id)) {
			return "redirect:/books";
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			model.addAttribute("book", book.get());
			return "book-edit";
		}
		return "redirect:/books";
	}

	@PostMapping("/books/{id}/edit")
	public String booksEditPost(@PathVariable(value = "id") long id,
								@RequestParam String isbn,
								Model model) {
		if (!bookRepository.existsById(id)) {
			return "redirect:/books";
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			long parsedIsbn = Long.parseLong(isbn.replace("-", ""));
			if (Book.validateIsbn(parsedIsbn)) {
				book.get().setIsbn(parsedIsbn);
				bookRepository.save(book.get());
				return "redirect:/books";
			}
		}
		return "redirect:/authors";
	}

	@PostMapping("/books/{id}/edit/author")
	public String bookEditAuthorPost(@PathVariable(value = "id") long id, @RequestParam long authorId) {
		if (!bookRepository.existsById(id)) {
			return "redirect:/books/" + id + "/edit";
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			Optional<Author> foundAuthor = authorRepository.findById(authorId);
			if (foundAuthor.isEmpty()) {
				return "redirect:/books/" + id + "/edit";
			}
			Set<Author> bookAuthors = book.get().getAuthors();
			if (bookAuthors.contains(foundAuthor.get())) {
				bookAuthors.remove(foundAuthor.get());
			} else {
				bookAuthors.add(foundAuthor.get());
			}
			authorRepository.save(foundAuthor.get());
		}
		return "redirect:/books/" + id + "/edit";
	}

	@PostMapping("/books/{id}/edit/genre")
	public String bookEditGenrePost(@PathVariable(value = "id") long id, @RequestParam long genreId) {
		if (!bookRepository.existsById(id)) {
			return "redirect:/books/" + id + "/edit";
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			Optional<Genre> foundGenre = genreRepository.findById(genreId);
			if (foundGenre.isEmpty()) {
				return "redirect:/books/" + id + "/edit";
			}
			Set<Genre> bookGenres = book.get().getGenres();
			if (bookGenres.contains(foundGenre.get())) {
				bookGenres.remove(foundGenre.get());
			} else {
				bookGenres.add(foundGenre.get());
			}
			genreRepository.save(foundGenre.get());
		}
		return "redirect:/books/" + id + "/edit";
	}

	@PostMapping("/books/{id}/delete")
	public String bookRemovePost(@PathVariable(value = "id") long id, Model model) {
		if (!bookRepository.existsById(id)) {
			return "redirect:/books";
		}
		Optional<Book> book = bookRepository.findById(id);
		book.ifPresent(bookRepository::delete);
		return "redirect:/books";
	}

	@ExceptionHandler(InputDataParseException.class)
	public String handleException(InputDataParseException exception) {
		this.message = exception.getMessage();
		return "redirect:" + exception.getUri();
	}
}
