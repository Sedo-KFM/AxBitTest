package com.sedo.AxBitTest.controllers;

import ch.qos.logback.classic.Logger;
import com.sedo.AxBitTest.exceptions.IncorrectIdException;
import com.sedo.AxBitTest.exceptions.InputDataValidateException;
import com.sedo.AxBitTest.exceptions.ViolatedDataException;
import com.sedo.AxBitTest.helpers.MessageToModelTransfer;
import com.sedo.AxBitTest.models.Author;
import com.sedo.AxBitTest.models.Book;
import com.sedo.AxBitTest.models.Genre;
import com.sedo.AxBitTest.repo.AuthorRepository;
import com.sedo.AxBitTest.repo.BookRepository;
import com.sedo.AxBitTest.repo.GenreRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.Set;

@Controller
public class BookController {

	static private final Logger logger = (Logger) LoggerFactory.getLogger(MainController.class);
	private final StringBuilder message = new StringBuilder("");

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private GenreRepository genreRepository;

	@GetMapping("/books")
	public String books(Model model) {
		logger.trace("GET /books");
		MessageToModelTransfer.transferMessage(this.message, model);
		Iterable<Book> books = bookRepository.findAll();
		model.addAttribute("books", books);
		return "books";
	}

	@GetMapping("/books/adding")
	public String booksAdd(Model model) {
		logger.trace("GET /books/adding");
		MessageToModelTransfer.transferMessage(this.message, model);
		return "books-adding";
	}

	@PostMapping("/books/adding")
	public String booksAddPost(@RequestParam String isbn, Model model) {
		logger.trace("POST /books/adding isbn=\"{}\"", isbn);
		long parsedIsbn;
		try {
			parsedIsbn = Long.parseLong(isbn);
		} catch (NumberFormatException numberFormatException) {
			throw new InputDataValidateException("/books/adding", "ISBN должен состоять только из цифр");
		}
		if (Long.toString(parsedIsbn).length() != 13) {
			throw new InputDataValidateException("/books/adding", "ISBN должен состоять из 13 цифр");
		}
		Book book = new Book(parsedIsbn);
		bookRepository.save(book);
		return "redirect:/books";
	}

	@GetMapping("/books/{id}")
	public String book(@PathVariable(value = "id") long id, Model model) {
		logger.trace("GET /books/{}", id);
		MessageToModelTransfer.transferMessage(this.message, model);
		if (!bookRepository.existsById(id)) {
			throw new IncorrectIdException("/books", "Этой книги уже не существует");
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			model.addAttribute("book", book.get());
			return "book";
		}
		throw new ViolatedDataException("/books", "Данные книги нарушены");
	}

	@GetMapping("/books/{id}/editing")
	public String bookEdit(@PathVariable(value = "id") long id, Model model) {
		logger.trace("GET /books/{}/editing", id);
		MessageToModelTransfer.transferMessage(this.message, model);
		if (!bookRepository.existsById(id)) {
			throw new IncorrectIdException("/books", "Этой книги уже не существует");
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			model.addAttribute("book", book.get());
			return "book-editing";
		}
		throw new ViolatedDataException("/books", "Данные книги нарушены");
	}

	@PostMapping("/books/{id}/editing")
	public String booksEditPost(@PathVariable(value = "id") long id,
								@RequestParam String isbn,
								Model model) {
		logger.trace("POST /books/{}/editing isbn=\"{}\"" , id, isbn);
		if (!bookRepository.existsById(id)) {
			throw new IncorrectIdException("/books", "Этой книги уже не существует");
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			long parsedIsbn = Long.parseLong(isbn.replace("-", ""));
			if (Book.validateIsbn(parsedIsbn)) {
				book.get().setIsbn(parsedIsbn);
				bookRepository.save(book.get());
				return "redirect:/books";
			}
			throw new InputDataValidateException("/books/" + id + "/editing", "ISBN некорректен");
		}
		throw new ViolatedDataException("/books", "Данные книги нарушены");
	}

	@PostMapping("/books/{id}/editing/author")
	public String bookEditAuthorPost(@PathVariable(value = "id") long id, @RequestParam long authorId) {
		logger.trace("POST /books/{}/editing/author authorId={}", id, authorId);
		if (!bookRepository.existsById(id)) {
			throw new IncorrectIdException("/books", "Этой книги уже не существует");
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			Optional<Author> foundAuthor = authorRepository.findById(authorId);
			if (foundAuthor.isEmpty()) {
				throw new IncorrectIdException("/books/" + id +"/editing", "Указанного автора не существует");
			}
			Set<Author> bookAuthors = book.get().getAuthors();
			if (bookAuthors.contains(foundAuthor.get())) {
				bookAuthors.remove(foundAuthor.get());
			} else {
				bookAuthors.add(foundAuthor.get());
			}
			authorRepository.save(foundAuthor.get());
		}
		throw new ViolatedDataException("/books", "Данные книги нарушены");
	}

	@PostMapping("/books/{id}/editing/genre")
	public String bookEditGenrePost(@PathVariable(value = "id") long id, @RequestParam long genreId) {
		logger.trace("POST /books/{}/editing/genre genreId={}", id, genreId);
		if (!bookRepository.existsById(id)) {
			throw new IncorrectIdException("/books", "Этой книги уже не существует");
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			Optional<Genre> foundGenre = genreRepository.findById(genreId);
			if (foundGenre.isEmpty()) {
				throw new IncorrectIdException("/books/" + id +"/editing", "Указанного жанра не существует");
			}
			Set<Genre> bookGenres = book.get().getGenres();
			if (bookGenres.contains(foundGenre.get())) {
				bookGenres.remove(foundGenre.get());
			} else {
				bookGenres.add(foundGenre.get());
			}
			genreRepository.save(foundGenre.get());
		}
		throw new ViolatedDataException("/books", "Данные книги нарушены");
	}

	@PostMapping("/books/{id}/delete")
	public String bookRemovePost(@PathVariable(value = "id") long id, Model model) {
		logger.trace("POST /books/{}/delete", id);
		if (!bookRepository.existsById(id)) {
			throw new IncorrectIdException("/books", "Этой книги уже не существует");
		}
		Optional<Book> book = bookRepository.findById(id);
		if (book.isPresent()) {
			bookRepository.delete(book.get());
			return "redirect:/books";
		}
		throw new ViolatedDataException("/books", "Данные книги нарушены");
	}

	@ExceptionHandler(InputDataValidateException.class)
	public String handleException(InputDataValidateException exception) {
		logger.warn("EXCEPTION \"{}\"", exception.getMessage());
		this.message.append(exception.getMessage());
		return "redirect:" + exception.getUri();
	}

	@ExceptionHandler(IncorrectIdException.class)
	public String handleException(IncorrectIdException exception) {
		logger.warn("EXCEPTION \"{}\"", exception.getMessage());
		this.message.append(exception.getMessage());
		return "redirect:" + exception.getUri();
	}

	@ExceptionHandler(ViolatedDataException.class)
	public String handleException(ViolatedDataException exception) {
		logger.warn("EXCEPTION \"{}\"", exception.getMessage());
		this.message.append(exception.getMessage());
		return "redirect:" + exception.getUri();
	}
}
