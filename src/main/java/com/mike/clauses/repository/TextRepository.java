package com.mike.clauses.repository;

import com.mike.clauses.model.Clause;
import com.mike.clauses.model.Text;
import com.mike.clauses.model.WordBook;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TextRepository extends CrudRepository<Text,Long>{
//    Text findByWord(WordBook wordBook);
//    Text getByWord(WordBook wordBook);
    List<Text> findAllByWord(WordBook wordBook);
}
