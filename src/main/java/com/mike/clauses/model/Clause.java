package com.mike.clauses.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.mike.clauses.model.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Clause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne (fetch=FetchType.LAZY)
//    @ManyToMany
    @JoinColumn // (name="ARTICLE_ID")
    @JsonIgnore
    private Article article;

//    @ManyToOne (fetch=FetchType.LAZY)
    @ManyToMany
    @JoinColumn // (name="ARTICLE_ID")
    @JsonIgnore
    private List<Article> articles;

    @JsonIgnore
    @Column(columnDefinition="TEXT")
    private String text;

    @JsonIgnore
    @OneToMany (mappedBy="clause", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Text> words;

    @JsonIgnore
    private Integer number;

    public Clause(Article article){
        this.article=article;
        this.text="";
    }

    public Clause(Article article, String text){
        this.article=article;
        number=article.getCount();
        article.setCount(number+1);
        this.text=text;
    }

}
