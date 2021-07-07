package com.sedo.AxBitTest.models;

import com.sedo.AxBitTest.helpers.SetHelper;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
public class Genre extends TimestampingModel{

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

	public Genre() {
		super();
	}

	public Genre(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}
}
