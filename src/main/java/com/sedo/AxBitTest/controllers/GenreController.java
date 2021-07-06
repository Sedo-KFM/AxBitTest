package com.sedo.AxBitTest.controllers;

import com.sedo.AxBitTest.models.Genre;
import com.sedo.AxBitTest.models.Book;
import com.sedo.AxBitTest.repo.GenreRepository;
import com.sedo.AxBitTest.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;
import java.util.Set;

@Controller
public class GenreController {

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/genres")
	public String genres(Model model) {
		Iterable<Genre> genres = genreRepository.findAll();
		model.addAttribute("genres", genres);
		return "genres";
	}

	@GetMapping("/genres/add")
	public String genresAdd(Model model) {
		return "genres-add";
	}

	@PostMapping("/genres/add")
	public String genresAddPost(@RequestParam String name,
								 Model model) {
		Genre genre = new Genre(name);
		genreRepository.save(genre);
		return "redirect:/genres";
	}

	@GetMapping("/genres/{id}")
	public String genre(@PathVariable(value = "id") long id, Model model) {
		if (!genreRepository.existsById(id)) {
			return "redirect:/genres";
		}
		Optional<Genre> genre = genreRepository.findById(id);
		if (genre.isPresent()) {
			model.addAttribute("genre", genre.get());
			return "genre";
		}
		return "redirect:/genres";
	}

	@GetMapping("/genres/{id}/edit")
	public String genreEdit(@PathVariable(value = "id") long id, Model model) {
		if (!genreRepository.existsById(id)) {
			return "redirect:/genres";
		}
		Optional<Genre> genre = genreRepository.findById(id);
		if (genre.isPresent()) {
			model.addAttribute("genre", genre.get());
			return "genre-edit";
		}
		return "redirect:/genres";
	}

	@PostMapping("/genres/{id}/edit")
	public String genresEditPost(@PathVariable(value = "id") long id,
								  @RequestParam String name,
								  Model model) {
		if (!genreRepository.existsById(id)) {
			return "redirect:/genres";
		}
		Optional<Genre> genre = genreRepository.findById(id);
		if (genre.isPresent()) {
			genre.get().setName(name);
			genreRepository.save(genre.get());
			return "redirect:/genres";
		}
		return "redirect:/genres";
	}

	@PostMapping("/genres/{id}/edit/book")
	public String genreEditBookPost(@PathVariable(value = "id") long id, @RequestParam long bookId) {
		if (!genreRepository.existsById(id)) {
			return "redirect:/genres/" + id + "/edit";
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
		return "redirect:/genres/" + id + "/edit";
	}

	@PostMapping("/genres/{id}/delete")
	public String genreRemovePost(@PathVariable(value = "id") long id, Model model) {
		if (!genreRepository.existsById(id)) {
			return "redirect:/genres";
		}
		Optional<Genre> genre = genreRepository.findById(id);
		genre.ifPresent(genreRepository::delete);
		return "redirect:/genres";
	}
}
