package com.mike.clauses.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    private String name;

    @JsonIgnore
    @OneToMany // (mappedBy="author", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Article> articles;

    public Author(String name) {this.name=name;}
}
