package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Author;
import com.nerdysoft.rest.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByTitleAndAuthor(String title, Author author);

}
