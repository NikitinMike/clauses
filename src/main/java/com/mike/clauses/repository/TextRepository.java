package com.mike.clauses.repository;

import com.mike.clauses.model.Text;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TextRepository extends CrudRepository<Text,Long>{
}
