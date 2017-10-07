package com.quoteservice.apigateway.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@jsonid")
public class Quote {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;


    private String text;

    private String source;
    
    private Long authorId;
    

    public Quote() {}

    public Quote(String text, String source, long i) {
        this.text = text;
        this.source = source;
        this.authorId=i;
    }

    @Override
    public String toString() {
        return String.format("Quote[id=%d, text='%s'", this.id, this.text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    
    public Long getAuthorId(){
    	return authorId;
    }
    
    public void setAuthor(Long authord) {
        this.authorId = id;
    }

    public Long getId() {
        return id;
    }
    
    
}