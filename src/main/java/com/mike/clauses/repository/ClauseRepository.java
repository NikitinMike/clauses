package com.mike.clauses.repository;

import org.springframework.data.repository.CrudRepository;
import com.mike.clauses.model.*;

import java.util.List;

public interface ClauseRepository extends CrudRepository<Clause,Long>{
    List<Clause> findAllByArticle(Article article);
    List<Clause> findAllBy();
}
