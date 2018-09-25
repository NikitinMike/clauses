package com.mike.clauses.repository;

import com.mike.clauses.model.WordBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WordBookRepository extends PagingAndSortingRepository<WordBook,Long> {  // CrudRepository
//public interface WordBookRepository extends CrudRepository<WordBook,Long>{
    @Override
    Page<WordBook> findAll(Pageable pageable);
    List<WordBook> findAllBy();
    WordBook findByWord(String word);
}
