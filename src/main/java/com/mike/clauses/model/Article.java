package com.mike.clauses.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.mike.clauses.*;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Order
    private String title;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn // (name="AUTHOR_ID")
//    @JsonIgnore
    private Author author;

    @JsonIgnore
    @OneToMany (mappedBy="article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Clause> clauses;

    private int count;

    public Article(Author author,String title){
        this.author=author;
        this.title=title;
        this.count=0;
    }

}
