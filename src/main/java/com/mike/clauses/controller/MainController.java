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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.random;
import static java.time.LocalTime.now;
import static org.aspectj.util.LangUtil.split;
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

//    @ResponseBody
    @RequestMapping({"/add/"})
    String add(Model model){
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
    @RequestMapping({"/","/article/"})
    String mainHome(Model model){
//        System.out.println(author.name);
//        addClauses(article,3);
//        return now().toString();
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
        }) @PageableDefault(size = 333) org.springframework.data.domain.Pageable pageable)
    {
        model.addAttribute("pages", words.count()/pageable.getPageSize());
        model.addAttribute("words", words.findAll(pageable));
        return "words";
    }

//    @ResponseBody
    @RequestMapping("/article/{id}")
//    List<Clause>
    String article(@PathVariable Long id,Model model){
        Article article=articles.getById(id);
        if(article==null) return null;
//        return clauses.findAllByArticle(articles.getById(1L));
//        System.out.println(article.getTitle());
//        System.out.println(article.getAuthor().getName());

        model.addAttribute("author", article.getAuthor().getName());
        model.addAttribute("title", article.getTitle());
        model.addAttribute("clauses", article.getClauses());
        return "article";

//        return article.getClauses();
    }

    @ResponseBody
    @RequestMapping("/word/{id}")
//    WordBook
    List<Text>
    word(@PathVariable Long id,Model model){
//        System.out.println(word.getById(id));
//        model.addAttribute("word", word.getById(id).getWords());
//        return "word";
        return text.findAllByWord(words.getById(id));
//        return clauses.findAllByWordsContaining(text.getByWord(words.getById(id)));
//        return words.getById(id);
    }

//    @ResponseBody
    @RequestMapping("/clause/{id}")
    String
//    List<Text>
    clause(@PathVariable Long id,Model model){
        System.out.println(clauses.getById(id).getArticles());
        model.addAttribute("clause", clauses.getById(id).getText());
        model.addAttribute("words", clauses.getById(id).getWords());
        return "clause";
//        return clauses.getById(id).getText();
//        return clauses.getById(id).getWords();
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

    private String getPage(String s){
//        System.out.println('['+s+']');
        StringBuilder result = new StringBuilder();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader((new URL(s).openConnection()).getInputStream(),"UTF-8"))) // "windows-1251"
        {for(String line;(line=rd.readLine())!=null;) result.append(line); rd.close();}
        catch (IOException e) {e.printStackTrace();}
        return result.toString();
    }

    @RequestMapping("random")
    String randomPage(){
        String href = "<a href=(.+?)>";
        String html = "<.*?>";
        String url="https://ru.wikipedia.org/wiki/%D0%A1%D0%BB%D1%83%D0%B6%D0%B5%D0%B1%D0%BD%D0%B0%D1%8F:%D0%A1%D0%BB%D1%83%D1%87%D0%B0%D0%B9%D0%BD%D0%B0%D1%8F_%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%B8%D1%86%D0%B0";
        String page=getPage(url);

//        System.out.println("=======================================================================================================================");
        Pattern pattern = Pattern.compile("<title>(.*)</title>");
        Matcher matcher = pattern.matcher(page);
        if (!matcher.find()) return "redirect:/";
        String [] title = matcher.group(1).split("—");
//        System.out.println(" *** "+title+" *** ");

        Author author=authorAdd(title[1].trim());
//        System.out.println(author.name);
        Article article=articleAdd(author,title[0].trim());

        page=page.replaceFirst(".+<div class=\"mw-parser-output\">","");
        page=page.replaceFirst("<noscript>.+","");
        page=page.replaceAll("<a.+?>","");
        page=page.replaceAll("<img.+?>","");
        page=page.replaceAll("<cpan.+?>","");
        page=page.replaceAll("<div.+?>","");
        page=page.replaceAll("<table.+?>.+?</table>","");
        page=page.replaceAll("<.*?>","!");
        page=page.replaceAll("[А-ЯЁ][.]","");
        page=page.replaceAll("[0-9]+[.][0-9]+[0-9]+[.]",""); // date
        page=page.replaceAll("[0-9]+[,.][0-9]+",""); // numeric
        page=page.replaceAll("160","");
//        page=page.replaceAll("править код","");
//        page=page.replaceAll("править","");

//        page=page.replaceAll("</.+?>","");
//        System.out.println(page.replaceAll("[^А-ЯЁа-яё]+"," "));
        for (String s : page.split("[.,!?()]")) {
            s=s.replaceAll("[^А-ЯЁа-яё0-9]+"," ").trim();
            if(s.replaceAll("[0-9]","").trim().isEmpty()) continue;
            if (!s.isEmpty() && (s.length()>1))
                {
//                  System.out.println("\n["+s+"] ");
//                  if(!s.replaceAll("[0-9]","").trim().isEmpty())
                    Clause clause = new Clause(article,s);
                    clauses.save(clause);
                    Long j=0L;
                    for(String w : s.replaceAll("[0-9]","").trim().toLowerCase().split(" "))
                        if(w.length()>1) {
//                                System.out.print(w+",");
                            WordBook word=words.findByWord(w);
                            if (word==null) words.save(word = new WordBook(w));
                            Text textitem=new Text(clause,word,j++);
                            text.save(textitem);
                        }
                    clauses.save(clause);
                }
        }

        return "redirect:/";
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

}
