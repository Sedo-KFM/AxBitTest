package com.sedo.AxBitTest.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long isbn;
//	@ManyToMany(mappedBy = "book")
//	private List<Author> authors;

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

//	public List<Author> getAuthors() {
//		return authors;
//	}
//
//	public void setAuthors(List<Author> authors) {
//		this.authors = authors;
//	}
}
