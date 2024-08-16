package com.nerdysoft.rest.controller;

import com.nerdysoft.rest.dto.AuthorDTO;
import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = "/books")
    public String books() {
        return "books";
    }

    @GetMapping(path = "/books/list")
    @ResponseBody
    public List<BookDTO> booksList() {
        return bookService.findAll();
    }

    @PostMapping("/book/create")
    public ResponseEntity<String> createBook(@RequestBody BookDTO book) {
        bookService.create(book);
        return ResponseEntity.ok("Book created successfully");
    }

    @PostMapping("/book/update")
    public ResponseEntity<String> updateBook(@RequestBody BookDTO book) {
        bookService.update(book);
        return ResponseEntity.ok("Book updated successfully");
    }

    @GetMapping("/book/find")
    @ResponseBody
    public List<BookDTO> findBooks(@RequestParam String title, @RequestParam String author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setName(author);

        if (!title.isEmpty() && !author.isEmpty()) {
            Optional<BookDTO> optional = bookService.findByTitleAndAuthor(title, authorDTO);
            if (optional.isPresent()) {
                return List.of(optional.get());
            }
        } else if (!title.isEmpty()) {
            return bookService.findByTitle(title);
        } else if (!author.isEmpty()) {
            return bookService.findByAuthor(authorDTO);
        }
        return new ArrayList<>();
    }

    @PostMapping("/book/delete")
    public ResponseEntity<String> deleteBook(@RequestBody BookDTO book,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
        }

        bookService.delete(book);
        return ResponseEntity.ok("Book deleted successfully");
    }

    @GetMapping("/book/distinct")
    @ResponseBody
    public List<BookDTO> distinctBooks() {
        return bookService.findAllDistinct();
    }

    @GetMapping("/book/distinct/borrowed")
    @ResponseBody
    public List<BookDTO> borrowedBooks() {
        return bookService.findAllBorrowedDistinct();
    }
}
