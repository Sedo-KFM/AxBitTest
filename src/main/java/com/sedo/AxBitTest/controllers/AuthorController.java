package com.sedo.AxBitTest.controllers;

import ch.qos.logback.classic.Logger;
import com.sedo.AxBitTest.exceptions.IncorrectIdException;
import com.sedo.AxBitTest.exceptions.InputDataValidateException;
import com.sedo.AxBitTest.exceptions.ViolatedDataException;
import com.sedo.AxBitTest.helpers.MessageToModelTransfer;
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

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

@Controller
public class AuthorController {

	static private final Logger logger = (Logger) LoggerFactory.getLogger(MainController.class);
	private final StringBuilder message = new StringBuilder("");
	private Long lastAddedAuthorId = null;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/authors")
	public String authors(Model model) {
		logger.trace("GET /authors");
		MessageToModelTransfer.transferMessage(this.message, model);
		Iterable<Author> authors = authorRepository.findAll();
		model.addAttribute("authors", authors);
		return "authors";
	}

	@GetMapping("/authors/add")
	public String authorsAdd(Model model) {
		logger.trace("GET /authors/add");
		MessageToModelTransfer.transferMessage(this.message, model);
		this.lastAddedAuthorId = null;
		return "authors-add";
	}

	@PostMapping("/authors/add")
	public String authorsAddPost(@RequestParam String surname,
								 @RequestParam String name,
								 @RequestParam String patronymic,
								 @RequestParam Date birthdate,
								 Model model) {
		logger.trace("POST /authors/add " +
				"surname=\"{}\" " +
				"name=\"{}\" " +
				"patronymic=\"{}\" " +
				"birthdate=\"{}\"",
				surname, name, patronymic, birthdate);
		if (surname.equals("") || name.equals("") || patronymic.equals("") || birthdate == null) {
			throw new InputDataValidateException("/authors/add", "Все поля должны быть заполнены");
		}
		Author author = new Author(name, surname, patronymic, birthdate);
		authorRepository.save(author);
		return "redirect:/authors";
	}

	@GetMapping("/authors/{id}")
	public String author(@PathVariable(value = "id") long id, Model model) {
		logger.trace("GET /authors/{}", id);
		MessageToModelTransfer.transferMessage(this.message, model);
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

	@GetMapping("/authors/{id}/edit")
	public String authorEdit(@PathVariable(value = "id") long id, Model model) {
		logger.trace("GET /authors/{}/edit", id);
		MessageToModelTransfer.transferMessage(this.message, model);
		if (!authorRepository.existsById(id)) {
			throw new IncorrectIdException("/authors", "Этого автора уже не существует");
		}
		this.lastAddedAuthorId = id;
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			model.addAttribute("author", author.get());
			return "author-edit";
		}
		throw new ViolatedDataException("/authors", "Данные автора нарушены");
	}

	@PostMapping("/authors/{id}/edit")
	public String authorsEditPost(@PathVariable(value = "id") long id,
								  @RequestParam String surname,
								  @RequestParam String name,
								  @RequestParam String patronymic,
								  @RequestParam Date birthdate,
								  Model model) {
		logger.trace("POST /authors/{}/edit " +
						"surname=\"{}\" " +
						"name=\"{}\" " +
						"patronymic=\"{}\" " +
						"birthdate=\"{}\"",
				id, surname, name, patronymic, birthdate);
		MessageToModelTransfer.transferMessage(this.message, model);
		if (!authorRepository.existsById(id)) {
			throw new IncorrectIdException("/authors", "Этого автора уже не существует");
		}
		if (surname.equals("") || name.equals("") || patronymic.equals("") || birthdate == null) {
			throw new InputDataValidateException("/authors/" + id + "/edit", "Все поля должны быть заполнены");
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			author.get().edit(name, surname, patronymic, birthdate);
			authorRepository.save(author.get());
			return "redirect:/authors";
		}
		throw new ViolatedDataException("/authors", "Данные автора нарушены");
	}

	@PostMapping("/authors/{id}/edit/book")
	public String authorEditBookPost(@PathVariable(value = "id") long id, @RequestParam Long bookId) {
		logger.trace("POST /authors/{}/edit/book bookId={}", id, bookId);
		if (!authorRepository.existsById(id)) {
			throw new IncorrectIdException("/authors", "Этого автора уже не существует");
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			if (bookId == null) {
				throw new IncorrectIdException("/authors/" + id +"/edit/book", "ага ща");
			}
			Optional<Book> foundBook = bookRepository.findById(bookId);
			if (foundBook.isEmpty()) {
				throw new IncorrectIdException("/authors/" + id + "/edit", "Указанной книги не существует");
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

	@PostMapping("/authors/{id}/delete")
	public String authorRemovePost(@PathVariable(value = "id") long id, Model model) {
		logger.trace("POST /authors/{}/delete", id);
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

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public String handleException(MethodArgumentTypeMismatchException exception) {
		logger.warn("EXCEPTION: \"Incorrect input date\"");
		this.message.append(("Некорректная дата"));
		if (this.lastAddedAuthorId == null) {
			return "redirect:/authors/add";
		} else {
			return "redirect:/authors/" + this.lastAddedAuthorId + "/edit";
		}
	}
}
