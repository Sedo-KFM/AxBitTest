package com.sedo.AxBitTest.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long isbn;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "book_author",
			joinColumns = @JoinColumn(name = "book_id"),
			inverseJoinColumns = @JoinColumn(name = "author_id")
	)
	private Set<Author> authors;

	public Book() {
	}

	public Book(Long isbn) {
		this.isbn = isbn;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}

	static public boolean validateIsbn(long isbn) {
		return Long.toString(isbn).length() == 13;
	}
}
