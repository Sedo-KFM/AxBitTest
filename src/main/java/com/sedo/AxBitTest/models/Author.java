package com.sedo.AxBitTest.models;

import com.sedo.AxBitTest.helpers.SetHelper;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
public class Author extends TimestampingModel{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name, surname, patronymic;
	private LocalDate birthdate;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "book_author",
			joinColumns = @JoinColumn(name = "author_id"),
			inverseJoinColumns = @JoinColumn(name = "book_id")
	)
	private Set<Book> books;

	public Author() {
		super();
	}

	public Author(String name, String surname, String patronymic, LocalDate birthdate) {
		this.name = name;
		this.surname = surname;
		this.patronymic = patronymic;
		this.birthdate = birthdate;
	}

	public void edit(String name, String surname, String patronymic, LocalDate birthdate) {
		this.name = name;
		this.surname = surname;
		this.patronymic = patronymic;
		this.birthdate = birthdate;
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

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Author author = (Author) o;
		return id.equals(author.id) && name.equals(author.name) && surname.equals(author.surname) && Objects.equals(patronymic, author.patronymic) && birthdate.equals(author.birthdate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, surname, patronymic, birthdate);
	}

	public String getFullName() {
		return this.surname + " " + this.name.charAt(0) + ". " + this.patronymic.charAt(0) + ".";
	}
}

