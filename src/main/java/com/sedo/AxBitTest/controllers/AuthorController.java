package com.sedo.AxBitTest.controllers;

import com.sedo.AxBitTest.models.Author;
import com.sedo.AxBitTest.models.Book;
import com.sedo.AxBitTest.repo.AuthorRepository;
import com.sedo.AxBitTest.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.Date;
import java.util.Optional;
import java.util.Set;

@Controller
public class AuthorController {

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	@GetMapping("/authors")
	public String authors(Model model) {
		Iterable<Author> authors = authorRepository.findAll();
		model.addAttribute("authors", authors);
		return "authors";
	}

	@GetMapping("/authors/add")
	public String authorsAdd(Model model) {
		return "authors-add";
	}

	@PostMapping("/authors/add")
	public String authorsAddPost(@RequestParam String surname,
								 @RequestParam String name,
								 @RequestParam String patronymic,
								 @RequestParam Date birthdate,
								 Model model) {
		Author author = new Author(name, surname, patronymic, birthdate);
		authorRepository.save(author);
		return "redirect:/authors";
	}

	@GetMapping("/authors/{id}")
	public String author(@PathVariable(value = "id") long id, Model model) {
		if (!authorRepository.existsById(id)) {
			return "redirect:/authors";
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			model.addAttribute("author", author.get());
			return "author";
		}
		return "redirect:/authors";
	}

	@GetMapping("/authors/{id}/edit")
	public String authorEdit(@PathVariable(value = "id") long id, Model model) {
		if (!authorRepository.existsById(id)) {
			return "redirect:/authors";
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			model.addAttribute("author", author.get());
			return "author-edit";
		}
		return "redirect:/authors";
	}

	@PostMapping("/authors/{id}/edit")
	public String authorsEditPost(@PathVariable(value = "id") long id,
								  @RequestParam String surname,
								  @RequestParam String name,
								  @RequestParam String patronymic,
								  @RequestParam Date birthdate,
								  Model model) {
		if (!authorRepository.existsById(id)) {
			return "redirect:/authors";
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			author.get().edit(name, surname, patronymic, birthdate);
			authorRepository.save(author.get());
			return "redirect:/authors";
		}
		return "redirect:/authors";
	}

	@PostMapping("/authors/{id}/edit/book")
	public String authorEditBookPost(@PathVariable(value = "id") long id, @RequestParam String isbn) {
		long parsedIsbn = Long.parseLong(isbn.replace("-", ""));
		if (!Book.validateIsbn(parsedIsbn)) {
			return "redirect:/authors/" + id + "/edit";
		}
		if (!authorRepository.existsById(id)) {
			return "redirect:/authors/" + id + "/edit";
		}
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			Iterable<Book> allBooks = bookRepository.findAll();
			Book foundBook = null;
			boolean bookExists = false;
			for (Book book : allBooks) {
				if (book.getIsbn() == parsedIsbn) {
					bookExists = true;
					foundBook = book;
					break;
				}
			}
			if (!bookExists) {
				return "redirect:/authors/" + id + "/edit";
			}
			Set<Book> authorBooks = author.get().getBooks();
			boolean alreadyHave = false;
			for (Book book : authorBooks) {
				if (book.getIsbn() == parsedIsbn) {
					alreadyHave = true;
					foundBook = book;
				}
			}
			if (alreadyHave) {
				authorBooks.remove(foundBook);
			} else {
				authorBooks.add(foundBook);
			}
			authorRepository.save(author.get());
		}
		return "redirect:/authors/" + id + "/edit";
	}

	@PostMapping("/authors/{id}/delete")
	public String authorRemovePost(@PathVariable(value = "id") long id, Model model) {
		if (!authorRepository.existsById(id)) {
			return "redirect:/authors";
		}
		Optional<Author> author = authorRepository.findById(id);
		author.ifPresent(authorRepository::delete);
		return "redirect:/authors";
	}
}
