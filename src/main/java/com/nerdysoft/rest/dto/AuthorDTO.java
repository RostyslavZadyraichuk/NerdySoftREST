package com.nerdysoft.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AuthorDTO {

    private int id;

    @NotNull(message = "Author can't have empty name")
    @Pattern(regexp = "(^[A-Z][a-z]{2,}\\s[A-Z][a-z]{2,}$)", message = "Author name is incorrect")
    private String name;

    public AuthorDTO() {
    }

    public AuthorDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
