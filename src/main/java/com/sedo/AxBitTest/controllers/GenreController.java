package com.sedo.AxBitTest.controllers;

import ch.qos.logback.classic.Logger;
import com.sedo.AxBitTest.exceptions.IncorrectIdException;
import com.sedo.AxBitTest.exceptions.InputDataValidateException;
import com.sedo.AxBitTest.exceptions.ViolatedDataException;
import com.sedo.AxBitTest.models.Genre;
import com.sedo.AxBitTest.models.Book;
import com.sedo.AxBitTest.repo.GenreRepository;
import com.sedo.AxBitTest.repo.BookRepository;
import com.sun.istack.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@Controller
public class GenreController {

	static private final Logger logger = (Logger) LoggerFactory.getLogger(GenreController.class);

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("genres")
	public String genresGet(Model model) {
		logger.trace("GET /genres");
		Iterable<Genre> genres = genreRepository.findAll();
		model.addAttribute("genres", genres);
		return "genres";
	}

	@GetMapping("genres/adding")
	public String genresAddingGet(Model model) {
		logger.trace("GET /genres/adding");
		return "genres-adding";
	}

	@PostMapping("genres")
	public String genresPost(@RequestParam @NotNull String name,
								 Model model) {
		logger.trace("POST /genres name=\"{}\"", name);
		if (name.equals("")) {
			throw new InputDataValidateException("/genres/adding", "Все поля должны быть заполнены");
		}
		Genre genre = new Genre(name);
		genreRepository.save(genre);
		return "redirect:/genres";
	}

	@GetMapping("genres/{id}")
	public String genreGet(@PathVariable(value = "id") long id, Model model) {
		logger.trace("GET /genres/{}", id);
		if (!genreRepository.existsById(id)) {
			throw new IncorrectIdException("/genres", "Этого жанры уже не существует");
		}
		Optional<Genre> genre = genreRepository.findById(id);
		if (genre.isPresent()) {
			model.addAttribute("genre", genre.get());
			return "genre";
		}
		throw new ViolatedDataException("/genres", "Данные жанры нарушены");
	}

	@PostMapping("genres/{id}/editing")
	public String genreEditingPost(@PathVariable(value = "id") long id,
								  @RequestParam String name,
								  Model model) {
		logger.trace("POST /genres/{}/editing name=\"{}\"", id, name);
		if (!genreRepository.existsById(id)) {
			throw new IncorrectIdException("/genres", "Этого жанры уже не существует");
		}
		Optional<Genre> genre = genreRepository.findById(id);
		if (genre.isPresent()) {
			genre.get().setName(name);
			genreRepository.save(genre.get());
			return "redirect:/genres";
		}
		throw new ViolatedDataException("/genres", "Данные жанры нарушены");
	}

	@PatchMapping("genres/{id}/book")
	public String genreBookPatch(@PathVariable(value = "id") long id, @RequestParam long bookId) {
		logger.trace("POST /genres/{}/books, bookId={}", id, bookId);
		if (!genreRepository.existsById(id)) {
			throw new IncorrectIdException("/genres/" + id +"/editing", "Этого жанры уже не существует");
		}
		Optional<Genre> genre = genreRepository.findById(id);
		if (genre.isPresent()) {
			Iterable<Book> allBooks = bookRepository.findAll();
			Optional<Book> foundBook = bookRepository.findById(bookId);
			if (foundBook.isEmpty()) {
				throw new IncorrectIdException("/genres/" + id + "/editing", "Данной книги не существует");
			}
			Set<Book> genreBooks = genre.get().getBooks();
			if (genreBooks.contains(foundBook.get())) {
				genreBooks.remove(foundBook.get());
			} else {
				genreBooks.add(foundBook.get());
			}
			genreRepository.save(genre.get());
			return "redirect:/genres/" + id + "/editing";
		}
		throw new ViolatedDataException("/genres/" + id + "/editing", "Данные жанры нарушены");
	}

	@DeleteMapping("genres/{id}")
	public String genreDelete(@PathVariable(value = "id") long id, Model model) {
		logger.trace("GET /genres/{}", id);
		if (!genreRepository.existsById(id)) {
			throw new IncorrectIdException("/genres", "Этого жанры уже не существует");
		}
		Optional<Genre> genre = genreRepository.findById(id);
		if (genre.isPresent()) {
			genreRepository.delete(genre.get());
			return "redirect:/genres/";
		}
		throw new ViolatedDataException("/genres", "Данные жанры нарушены");
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
}
