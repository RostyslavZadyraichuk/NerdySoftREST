package com.nerdysoft.rest.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "borrow")
public class Borrow implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Member member;

    public Borrow() {
    }

    public Borrow(int id, Book book, Member member) {
        this.id = id;
        this.book = book;
        this.member = member;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
