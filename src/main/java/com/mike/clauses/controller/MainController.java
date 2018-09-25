package com.mike.clauses.controller;

import com.mike.clauses.model.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.mike.clauses.repository.*;
import com.mike.clauses.model.*;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Random;

import static java.lang.Math.random;
import static java.time.LocalTime.now;
import static org.thymeleaf.util.StringUtils.randomAlphanumeric;

@AllArgsConstructor
//@Rest
@Controller
public class MainController {

    AuthorRepository authors;
    ArticleRepository articles;
    ClauseRepository clauses;
    WordBookRepository words;
    TextRepository text;
    final Random random = new Random();

    Author authorAdd(String name){
        Author author = authors.findByName(name);
        if(author==null) author=new Author(name);
        authors.save(author);
        return author;
    }

    Article articleAdd(Author author,String title){
        Article article = new Article(author,title);
        articles.save(article);
        return article;
    }

    List<Clause> addClauses(Article article,Integer n){
        for (int i=0;i<n;i++) {
            Clause clause = new Clause(article);
            clauses.save(clause);
            String clauseWords="";
            for (Long j=0L;j<n;j++) {
                String sting = randomAlphanumeric(random.nextInt(n)).replaceAll("[A-Z]","");
                if (sting.isEmpty()) continue;
                WordBook word=words.findByWord(sting);
                if (word==null) words.save(word = new WordBook(sting));
                clauseWords = clauseWords+sting+" ";
                Text textitem=new Text(clause,word,j);
                text.save(textitem);
            }
            if (clauseWords.isEmpty()) continue;
            clause.setText(clauseWords);
            clauses.save(clause);
        }
        articles.save(article);
        return article.getClauses();
    }

//    @ResponseBody
    @RequestMapping({"/","/article/","/add/"})
    String mainHome(Model model){
        Author author=authorAdd("Mike");
//        System.out.println(author.name);
        Article article=articleAdd(author,randomAlphanumeric(20).toLowerCase());
        System.out.println("Author:"+article.getAuthor().getName());
        System.out.println("Title:"+article.getTitle());
        List<Clause> clauses = addClauses(article,3);
//        addClauses(article,3);
//        return now().toString();
        System.out.println(clauses);
//        return article.getClauses();
//        return articles.findAllBy();
        model.addAttribute("articles", articles.findAllBy());
        return "articles";
    }

//    @ResponseBody
//    @Controller
    @RequestMapping({"articles"})
    String getArticles(Model model){
//        articles.findAllBy().forEach(article->System.out.println(article.getTitle()+"/"+article.getAuthor().getName()));
        model.addAttribute("articles", articles.findAllBy());
//        model.addAttribute("articles", articleRepository.findAll(pageable));
        return "articles";
    }

    @RequestMapping({"authors"})
    String getAuthors(Model model){
        model.addAttribute("authors", authors.findAllBy());
        return "authors";
    }

    @RequestMapping({"clauses"})
    String getClauses(Model model){
        model.addAttribute("clauses", clauses.findAllBy());
        return "clauses";
    }

    @RequestMapping({"words"})
    String getWords(Model model,
        @SortDefault.SortDefaults({
          @SortDefault(sort = "word", direction = Sort.Direction.ASC),
//            @SortDefault(sort = "number", direction = Sort.Direction.ASC)
        }) @PageableDefault(size = 9) org.springframework.data.domain.Pageable pageable)
    {
        model.addAttribute("pages", words.count()/pageable.getPageSize());
        model.addAttribute("words", words.findAll(pageable));
        return "words";
    }

    @ResponseBody
    @RequestMapping("/article/{id}")
    List<Clause> article(@PathVariable Long id){
        Article article=articles.getById(id);
        if(article==null) return null;
//        return clauses.findAllByArticle(articles.getById(1L));
//        System.out.println(article.getTitle());
//        System.out.println(article.getAuthor().getName());
        return article.getClauses();
    }

    @ResponseBody
    @RequestMapping("/author/{id}")
    Author author(@PathVariable Long id){
        return authors.getById(id);
    }

    @RequestMapping("/add/{n}")
//    @ResponseBody
//    List<Clause>
//    Article
    String
    add(@PathVariable Integer n){
        Author author=authorAdd(randomAlphanumeric(10));
        Article article=articleAdd(author,randomAlphanumeric(20).toLowerCase());
//        System.out.println(now()+": "+article.getTitle()+" / "+article.getAuthor().getName());
        addClauses(article,n);
//        System.out.println("article:["+article.getClauses()+"]");
//        article.getClauses().forEach(clause->System.out.println(clause));
//        return "added:"+article.clauses+" / TITLE:"+article.title+" / AUTHOR:"+article.author.name+" "+now();
//        return clauses.findAllByArticle(article);
//        return article;
        return "redirect:/clauses/";
    }

}
