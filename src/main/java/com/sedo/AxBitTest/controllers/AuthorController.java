package com.sedo.AxBitTest.controllers;

import ch.qos.logback.classic.Logger;
import com.sedo.AxBitTest.exceptions.IncorrectIdException;
import com.sedo.AxBitTest.exceptions.InputDataValidateException;
import com.sedo.AxBitTest.exceptions.ViolatedDataException;
import com.sedo.AxBitTest.models.Author;
import com.sedo.AxBitTest.models.Book;
import com.sedo.AxBitTest.repo.AuthorRepository;
import com.sedo.AxBitTest.repo.BookRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Controller
public class AuthorController {

	static private final Logger logger = (Logger) LoggerFactory.getLogger(AuthorController.class);
	private Long lastAddedAuthorId = null;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("authors")
	public String authorsGet(Model model) {
		logger.trace("GET /authors");
		Iterable<Author> authors = authorRepository.findAll();
		model.addAttribute("authors", authors);
		return "authors";
	}

	@GetMapping("authors/adding")
	public String authorsAddingGet(Model model) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[0];
		logger.trace(stackTraceElement.getMethodName());
		logger.trace("GET /authors/adding");
		this.lastAddedAuthorId = null;
		return "authors-adding";
	}

	@PostMapping("authors")
	public String authorsPost(@RequestParam String surname,
								 @RequestParam String name,
								 @RequestParam String patronymic,
								 @RequestParam LocalDate birthdate,
								 Model model) {
		logger.trace("POST /authors " +
				"surname=\"{}\" " +
				"name=\"{}\" " +
				"patronymic=\"{}\" " +
				"birthdate=\"{}\"",
				surname, name, patronymic, birthdate);
		if (surname.equals("") || name.equals("") || patronymic.equals("") || birthdate == null) {
			throw new InputDataValidateException("/authors", "Все поля должны быть заполнены");
		}
		Author author = new Author(name, surname, patronymic, birthdate);
		authorRepository.save(author);
		return "redirect:/authors";
	}

	@GetMapping("authors/{id}")
	public String authorGet(@PathVariable(value = "id") long id, Model model) {
		logger.trace("GET /authors/{}", id);
		if (!authorRepository.existsById(id)) {
			throw new IncorrectIdException("/authors", "Этого автора уже не существует");
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			model.addAttribute("author", author.get());
			return "author";
		}
		throw new ViolatedDataException("/authors", "Данные автора нарушены");
	}

	@PutMapping("authors/{id}")
	public String authorPut(@PathVariable(value = "id") long id,
								  @RequestParam String surname,
								  @RequestParam String name,
								  @RequestParam String patronymic,
								  @RequestParam LocalDate birthdate,
								  Model model) {
		logger.trace("POST /authors/{} " +
						"surname=\"{}\" " +
						"name=\"{}\" " +
						"patronymic=\"{}\" " +
						"birthdate=\"{}\"",
				id, surname, name, patronymic, birthdate);
		if (!authorRepository.existsById(id)) {
			throw new IncorrectIdException("/authors", "Этого автора уже не существует");
		}
		if (surname.equals("") || name.equals("") || patronymic.equals("") || birthdate == null) {
			throw new InputDataValidateException("/authors/" + id + "/editing", "Все поля должны быть заполнены");
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			author.get().edit(name, surname, patronymic, birthdate);
			authorRepository.save(author.get());
			return "redirect:/authors";
		}
		throw new ViolatedDataException("/authors", "Данные автора нарушены");
	}

	@PatchMapping("authors/{id}/book")
	public String authorBookPatch(@PathVariable(value = "id") long id, @RequestParam Long bookId) {
		logger.trace("POST /authors/{}/book bookId={}", id, bookId);
		if (!authorRepository.existsById(id)) {
			throw new IncorrectIdException("/authors", "Этого автора уже не существует");
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			if (bookId == null) {
				throw new IncorrectIdException("/authors/" + id +"/book", "Не введён ID книги");
			}
			Optional<Book> foundBook = bookRepository.findById(bookId);
			if (foundBook.isEmpty()) {
				throw new IncorrectIdException("/authors/" + id + "/editing", "Указанной книги не существует");
			}
			Set<Book> authorBooks = author.get().getBooks();
			if (authorBooks.contains(foundBook.get())) {
				authorBooks.remove(foundBook.get());
			} else {
				authorBooks.add(foundBook.get());
			}
			authorRepository.save(author.get());
		}
		throw new ViolatedDataException("/authors", "Данные автора нарушены");
	}

	@DeleteMapping("authors/{id}")
	public String authorDelete(@PathVariable(value = "id") long id, Model model) {
		logger.trace("POST /authors/{}", id);
		if (!authorRepository.existsById(id)) {
			throw new IncorrectIdException("/authors", "Этого автора уже не существует");
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			authorRepository.delete(author.get());
			return "redirect:/authors";
		}
		throw new ViolatedDataException("/authors", "Данные автора нарушены");
	}

	@ExceptionHandler(InputDataValidateException.class)
	public String handleException(InputDataValidateException exception) {
		logger.warn("EXCEPTION \"{}\"", exception.getMessage());
		return "redirect:" + exception.getUri();
	}

	@ExceptionHandler(IncorrectIdException.class)
	public String handleException(IncorrectIdException exception) {
		logger.warn("EXCEPTION \"{}\"", exception.getMessage());
		return "redirect:" + exception.getUri();
	}

	@ExceptionHandler(ViolatedDataException.class)
	public String handleException(ViolatedDataException exception) {
		logger.warn("EXCEPTION \"{}\"", exception.getMessage());
		return "redirect:" + exception.getUri();
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public String handleException(MethodArgumentTypeMismatchException exception) {
		logger.warn("EXCEPTION: \"Incorrect input date\"");
		if (this.lastAddedAuthorId == null) {
			return "redirect:/authors/adding";
		} else {
			return "redirect:/authors/" + this.lastAddedAuthorId + "/editing";
		}
	}
}
