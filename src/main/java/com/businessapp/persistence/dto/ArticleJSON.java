package com.businessapp.persistence.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import com.businessapp.pojos.Article;
import com.businessapp.pojos.LogEntry;


/**
 * Private JSON DTO (Data Access Object) class that is associated with Article Pojo.
 */
class ArticleJSON extends Article implements JSONIntf {
    private static final long serialVersionUID = 1L;

    /**
     * Required by JSON de-serialization.
     */
    private ArticleJSON() {
        super( null, null );
        this.getNotes().clear();
    }


    /**
     * Public copy constructor to create JSON DTO from original POJO.
     * @param a copied Article object.
     */
    public ArticleJSON( Article a ) {
        super( a.getId(), a.getName() );
        for( String category : a.getCategory() ) {
            this.addCategory( category );
        }
        this.getNotes().clear();
        for( LogEntry le : a.getNotes() ) {
            this.getNotes().add( le );
        }
        this.setStatus( a.getStatus() );
    }

    /**
     * Public method to create original POJO from JSON DTO.
     * @return Article POJO.
     */
    @JsonIgnore
    public Article getArticle() {
        Article a = new Article( this.getId(), this.getName() );
        for( String category : this.getCategory() ) {
            a.addCategory( category );
        }
        a.getNotes().clear();
        for( LogEntry le : this.getNotes() ) {
            a.getNotes().add( le );
        }
        a.setStatus( this.getStatus() );
        return a;
    }


    /**
     * Article Json-Serializer for 'notes' property.
     * Maps notes as LogEntry array to String list.
     * @return notes as String list.
     */
    @JsonGetter("notes")
    public List<String> getNotesAsStringList() {
        List<String>res = new ArrayList<String>();
        for( LogEntry n : getNotes() ) {
            res.add( n.toString() );
        }
        return res;
    }

    /**
     * Custom Json-de-Serializer for 'notes' property.
     * Maps notes from String list to LogEntry array.
     * @param notesAsStr notes as String list.
     * @return self reference.
     */
    @JsonSetter("notes")
    public ArticleJSON setNotesAsStringList(String[] notesAsStr ) {
        for( String noteAsStr : notesAsStr ) {
            LogEntry note = new LogEntry( noteAsStr );
            getNotes().add( note );
        }
        return this;
    }

}
