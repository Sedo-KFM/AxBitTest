package com.sedo.AxBitTest.models;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name, surname, patronymic;
	private Date birthdate;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "book_author",
			joinColumns = @JoinColumn(name = "author_id"),
			inverseJoinColumns = @JoinColumn(name = "book_id")
	)
	private Set<Book> books;

	public Author() {
	}

	public Author(String name, String surname, String patronymic, Date birthdate) {
		this.name = name;
		this.surname = surname;
		this.patronymic = patronymic;
		this.birthdate = birthdate;
	}

	public void edit(String name, String surname, String patronymic, Date birthdate) {
		this.name = name;
		this.surname = surname;
		this.patronymic = patronymic;
		this.birthdate = birthdate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	public String getFullName() {
		return this.surname + " " + this.name.charAt(0) + ". " + this.patronymic.charAt(0) + ".";
	}
}
