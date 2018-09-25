package com.mike.clauses.repository;

import com.mike.clauses.model.WordBook;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WordBookRepository extends CrudRepository<WordBook,Long>{
    List<WordBook> findAllBy();
    WordBook findByWord(String word);
}
