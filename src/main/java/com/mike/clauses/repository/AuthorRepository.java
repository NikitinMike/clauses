package com.mike.clauses.repository;

import com.mike.clauses.model.Author;
import org.springframework.data.repository.CrudRepository;
import com.mike.clauses.*;

import java.util.List;

public interface AuthorRepository extends CrudRepository<Author,Long> {
    List<Author> findAll();
    Author getById(Long id);
    Author getFirstByIdIsNotNull();
    List<Author> findAllBy();

    Author findByName(String name);
}
