package com.sedo.AxBitTest.repo;

import com.sedo.AxBitTest.models.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}
