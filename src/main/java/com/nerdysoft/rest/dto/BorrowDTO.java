package com.nerdysoft.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class BorrowDTO {

    private int id;

    @NotNull(message = "Borrow can't be without book")
    @Valid
    private BookDTO book;

    @NotNull(message = "Borrow can't be without member")
    @Valid
    private MemberDTO member;

    public BorrowDTO() {
    }

    public BorrowDTO(int id, BookDTO book, MemberDTO member) {
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

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO bookDTO) {
        this.book = bookDTO;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO memberDTO) {
        this.member = memberDTO;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + id +
                ", book=" + book +
                ", member=" + member +
                '}';
    }
}
