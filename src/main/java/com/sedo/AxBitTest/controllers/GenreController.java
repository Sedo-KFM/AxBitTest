package com.sedo.AxBitTest.controllers;

import com.sedo.AxBitTest.exceptions.IncorrectIdException;
import com.sedo.AxBitTest.exceptions.InputDataValidateException;
import com.sedo.AxBitTest.exceptions.ViolatedDataException;
import com.sedo.AxBitTest.helpers.MessageToModelTransfer;
import com.sedo.AxBitTest.models.Genre;
import com.sedo.AxBitTest.models.Book;
import com.sedo.AxBitTest.repo.GenreRepository;
import com.sedo.AxBitTest.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@Controller
public class GenreController {

	private final StringBuilder message = new StringBuilder("");

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/genres")
	public String genres(Model model) {
		MessageToModelTransfer.transferMessage(this.message, model);
		Iterable<Genre> genres = genreRepository.findAll();
		model.addAttribute("genres", genres);
		return "genres";
	}

	@GetMapping("/genres/add")
	public String genresAdd(Model model) {
		MessageToModelTransfer.transferMessage(this.message, model);
		return "genres-add";
	}

	@PostMapping("/genres/add")
	public String genresAddPost(@RequestParam String name,
								 Model model) {
		if (name.equals("")) {
			throw new InputDataValidateException("/genres/add", "Все поля должны быть заполнены");
		}
		Genre genre = new Genre(name);
		genreRepository.save(genre);
		return "redirect:/genres";
	}

	@GetMapping("/genres/{id}")
	public String genre(@PathVariable(value = "id") long id, Model model) {
		MessageToModelTransfer.transferMessage(this.message, model);
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

	@GetMapping("/genres/{id}/edit")
	public String genreEdit(@PathVariable(value = "id") long id, Model model) {
		MessageToModelTransfer.transferMessage(this.message, model);
		if (!genreRepository.existsById(id)) {
			throw new IncorrectIdException("/genres", "Этого жанры уже не существует");
		}
		Optional<Genre> genre = genreRepository.findById(id);
		if (genre.isPresent()) {
			model.addAttribute("genre", genre.get());
			return "genre-edit";
		}
		throw new ViolatedDataException("/genres", "Данные жанры нарушены");
	}

	@PostMapping("/genres/{id}/edit")
	public String genresEditPost(@PathVariable(value = "id") long id,
								  @RequestParam String name,
								  Model model) {
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

	@PostMapping("/genres/{id}/edit/book")
	public String genreEditBookPost(@PathVariable(value = "id") long id, @RequestParam long bookId) {
		if (!genreRepository.existsById(id)) {
			throw new IncorrectIdException("/genres/" + id +"/edit", "Этого жанры уже не существует");
		}
		Optional<Genre> genre = genreRepository.findById(id);
		if (genre.isPresent()) {
			Iterable<Book> allBooks = bookRepository.findAll();
			Optional<Book> foundBook = bookRepository.findById(bookId);
			if (foundBook.isEmpty()) {
				return "redirect:/genres/" + id + "/edit";
			}
			Set<Book> genreBooks = genre.get().getBooks();
			if (genreBooks.contains(foundBook.get())) {
				genreBooks.remove(foundBook.get());
			} else {
				genreBooks.add(foundBook.get());
			}
			genreRepository.save(genre.get());
		}
		throw new ViolatedDataException("/genres/" + id + "/edit", "Данные жанры нарушены");
	}

	@PostMapping("/genres/{id}/delete")
	public String genreRemovePost(@PathVariable(value = "id") long id, Model model) {
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
		this.message.append(exception.getMessage());
		return "redirect:" + exception.getUri();
	}

	@ExceptionHandler(IncorrectIdException.class)
	public String handleException(IncorrectIdException exception) {
		this.message.append(exception.getMessage());
		return "redirect:" + exception.getUri();
	}

	@ExceptionHandler(ViolatedDataException.class)
	public String handleException(ViolatedDataException exception) {
		this.message.append(exception.getMessage());
		return "redirect:" + exception.getUri();
	}
}
