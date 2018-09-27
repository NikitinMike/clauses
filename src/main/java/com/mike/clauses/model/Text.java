package com.mike.clauses.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Text {
    @Id
    @GeneratedValue
    @JsonIgnore
    Long id;

    @Order
    Long pos;
    @OneToOne
    Clause clause;
    @OneToOne
    WordBook word;

    public Text(Clause clause,WordBook word,Long pos){
        this.clause=clause;
        this.word=word;
        this.pos=pos;
    }
}
