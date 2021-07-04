package com.sedo.AxBitTest.repo;

import com.sedo.AxBitTest.models.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
