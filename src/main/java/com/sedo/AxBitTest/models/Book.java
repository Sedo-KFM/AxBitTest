package com.sedo.AxBitTest.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Book extends TimestampingModel {

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
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "book_genre",
			joinColumns = @JoinColumn(name = "book_id"),
			inverseJoinColumns = @JoinColumn(name = "genre_id")
	)
	private Set<Genre> genres;

	public Book() {
		super();
	}

	public Book(Long isbn) {
		this.isbn = isbn;
	}

	public Long getId() {
		return id;
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

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
	}

	static public boolean validateIsbn(long isbn) {
		return Long.toString(isbn).length() == 13;
	}

	public String getStringedIsbn() {
		return (long) (this.isbn / Math.pow(10, 10))
				+ "-" + (long) (this.isbn / Math.pow(10, 9)) % (long) Math.pow(10, 1)
				+ "-" + (long) (this.isbn / Math.pow(10, 4)) % (long) Math.pow(10, 5)
				+ "-" + (this.isbn / 10) % (long) Math.pow(10, 3)
				+ "-" + this.isbn % 10;
	}
}
