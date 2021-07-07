package com.sedo.AxBitTest.models;

import com.sedo.AxBitTest.helpers.SetHelper;
import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;
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
	private final Date creationDate;
	private final Date modificationDate;

	public Author() {
		this.creationDate = new Date(System.currentTimeMillis());
		this.modificationDate = new Date(System.currentTimeMillis());
	}

	public Author(String name, String surname, String patronymic, Date birthdate) {
		this.name = name;
		this.surname = surname;
		this.patronymic = patronymic;
		this.birthdate = birthdate;
		this.creationDate = new Date(System.currentTimeMillis());
		this.modificationDate = new Date(System.currentTimeMillis());
	}

	public void edit(String name, String surname, String patronymic, Date birthdate) {
		this.name = name;
		this.surname = surname;
		this.patronymic = patronymic;
		this.birthdate = birthdate;
		this.modificationDate.setTime(System.currentTimeMillis());
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		if (!this.surname.equals(surname)) {
			this.surname = surname;
			this.modificationDate.setTime(System.currentTimeMillis());
		}
	}

	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		if (!this.patronymic.equals(patronymic)) {
			this.patronymic = patronymic;
			this.modificationDate.setTime(System.currentTimeMillis());
		}
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		if (!this.birthdate.equals(birthdate)) {
			this.birthdate = birthdate;
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
