package com.nerdysoft.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BookDTO {

    private int id;

    @NotBlank(message = "Book can't have empty title")
    @Pattern(regexp = "(^[A-Z].{2,}$)", message = "Book title is incorrect")
    private String title;

    @Min(value = 0, message = "Book's amount is incorrect")
    private int amount = 1;

    @NotNull(message = "Book can't be without author")
    @Valid
    private AuthorDTO author;

    public BookDTO() {
    }

    public BookDTO(int id, String title, int amount) {
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

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO authorDTO) {
        this.author = authorDTO;
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
