package com.sedo.AxBitTest.models;

import com.sedo.AxBitTest.helpers.SetHelper;

import javax.persistence.*;
import java.sql.Date;
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
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "book_genre",
			joinColumns = @JoinColumn(name = "book_id"),
			inverseJoinColumns = @JoinColumn(name = "genre_id")
	)
	private Set<Genre> genres;
	private final Date creationDate;
	private final Date modificationDate;

	public Book() {
		this.creationDate = new Date(System.currentTimeMillis());
		this.modificationDate = new Date(System.currentTimeMillis());
	}

	public Book(Long isbn) {
		this.isbn = isbn;
		this.creationDate = new Date(System.currentTimeMillis());
		this.modificationDate = new Date(System.currentTimeMillis());
	}

	public Long getId() {
		return id;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		if (!this.isbn.equals(isbn)) {
			this.isbn = isbn;
			this.modificationDate.setTime(System.currentTimeMillis());
		}
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		if (!this.authors.equals(authors)) {
			for (Author author : SetHelper.SetDifference(this.authors, authors)) {
				author.updateModificationDate();
			}
			this.authors = authors;
			this.modificationDate.setTime(System.currentTimeMillis());
		}
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		if (!this.genres.equals(genres)) {
			for (Genre genre : SetHelper.SetDifference(this.genres, genres)) {
				genre.updateModificationDate();
			}
			this.genres = genres;
			this.modificationDate.setTime(System.currentTimeMillis());
		}
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void updateModificationDate() {
		this.modificationDate.setTime(System.currentTimeMillis());
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
