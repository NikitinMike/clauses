package com.mike.clauses.repository;

import com.mike.clauses.model.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article,Long>
{
    Article getById(Long id);
    List<Article> findAllBy();
}
