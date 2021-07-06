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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getStringedIsbn() {
		return (long)(this.isbn / Math.pow(10, 10))
				+ "-" + (long)(this.isbn / Math.pow(10, 9)) % (long)Math.pow(10, 1)
				+ "-" + (long)(this.isbn / Math.pow(10, 4)) % (long)Math.pow(10, 5)
				+ "-" + (this.isbn / 10) % (long)Math.pow(10, 3)
				+ "-" + this.isbn % 10;
	}
}
