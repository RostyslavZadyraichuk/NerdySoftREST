package com.nerdysoft.rest.entity;

import jakarta.persistence.*;

@Entity(name = "book")
public class Book {

    @Id
    @GeneratedValue
    private int id;
    private String title;
    private int amount;
    @ManyToOne
    private Author author;

    public Book() {
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

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", author=" + author +
                '}';
    }
}
