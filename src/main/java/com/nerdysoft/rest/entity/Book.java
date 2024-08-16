package com.nerdysoft.rest.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "book")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int amount = 1;

    @ManyToOne
    private Author author;

    public Book() {
    }

    public Book(String title) {
        this.title = title;
    }

    public Book(String title, long borrowedCount) {
        this.title = title;
        this.amount = (int) borrowedCount;
    }

    public Book(int id, String title, int amount) {
        this.id = id;
        this.title = title;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

}
