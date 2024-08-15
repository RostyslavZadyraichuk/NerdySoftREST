package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Author;
import com.nerdysoft.rest.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByTitleAndAuthorName(String title, String author);

    List<Book> findByAuthorName(String author);

    List<Book> findByTitle(String title);

}
