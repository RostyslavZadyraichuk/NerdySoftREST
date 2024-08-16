package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByTitleAndAuthorName(String title, String author);

    List<Book> findByAuthorName(String author);

    List<Book> findByTitle(String title);

    @Query("SELECT DISTINCT new com.nerdysoft.rest.entity.Book(b.title) FROM book b")
    List<Book> findAllDistinctBooks();

    @Query("SELECT new com.nerdysoft.rest.entity.Book(b.title, COUNT(br.id)) " +
        "FROM book b RIGHT JOIN borrow br ON b.id = br.book.id " +
        "GROUP BY b.title")
    List<Book> findAllDistinctBorrowedBooks();
}
