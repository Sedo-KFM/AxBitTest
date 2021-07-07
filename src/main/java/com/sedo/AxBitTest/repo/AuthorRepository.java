package com.sedo.AxBitTest.repo;

import com.sedo.AxBitTest.models.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {
}
