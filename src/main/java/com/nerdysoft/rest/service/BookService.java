package com.nerdysoft.rest.service;

import com.nerdysoft.rest.dto.AuthorDTO;
import com.nerdysoft.rest.dto.BookDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface BookService {

    BookDTO create(@Valid BookDTO book);

    BookDTO update(@Valid BookDTO book);

    Optional<BookDTO> findById(int id);

    void delete(BookDTO book);

    List<BookDTO> findAll();

    List<BookDTO> findByAuthor(AuthorDTO author);

    List<BookDTO> findByTitle(String title);

    Optional<BookDTO> findByTitleAndAuthor(String title, AuthorDTO author);

    List<BookDTO> findAllDistinct();

    List<BookDTO> findAllBorrowedDistinct();

    void deleteAll();
}
