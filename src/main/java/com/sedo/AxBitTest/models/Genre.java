package com.sedo.AxBitTest.models;

import com.sedo.AxBitTest.helpers.SetHelper;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
public class Genre {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "book_genre",
			joinColumns = @JoinColumn(name = "genre_id"),
			inverseJoinColumns = @JoinColumn(name = "book_id")
	)
	private Set<Book> books;
	private final Date creationDate;
	private final Date modificationDate;

	public Genre() {
		this.creationDate = new Date(System.currentTimeMillis());
		this.modificationDate = new Date(System.currentTimeMillis());
	}

	public Genre(String name) {
		this.name = name;
		this.creationDate = new Date(System.currentTimeMillis());
		this.modificationDate = new Date(System.currentTimeMillis());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (!this.name.equals(name)) {
			this.name = name;
			this.modificationDate.setTime(System.currentTimeMillis());
		}
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		if (!this.books.equals(books)) {
			for (Book  book : SetHelper.SetDifference(this.books, books)) {
				book.updateModificationDate();
			}
			this.books = books;
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
}
