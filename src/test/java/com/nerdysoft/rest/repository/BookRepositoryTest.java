package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Author;
import com.nerdysoft.rest.entity.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BookRepositoryTest {

    private static Book book;

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;

    @Autowired
    public BookRepositoryTest(BookRepository bookRepo,
                              AuthorRepository authorRepo) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
    }

    @BeforeEach
    void beforeEach() {
        Author author = new Author();
        author.setName("John Doe");
        author = authorRepo.save(author);

        book = new Book();
        book.setTitle("Book Title");
        book.setAuthor(author);
        book = bookRepo.save(book);
    }

    @Test
    void titleValidation() {
        Book nullTitle = new Book();
        nullTitle.setTitle(null);
        nullTitle.setAuthor(book.getAuthor());
        assertThrows(DataIntegrityViolationException.class, () -> bookRepo.save(nullTitle));

        Book correctTitle = new Book();
        correctTitle.setTitle("Book Title");
        correctTitle.setAuthor(book.getAuthor());
        assertDoesNotThrow(() -> bookRepo.save(correctTitle));
        Optional<Book> optional = bookRepo.findById(correctTitle.getId());
        assertTrue(optional.isPresent());
    }

    @Test
    void createBook() {
        bookRepo.deleteAll();
        List<Book> books = bookRepo.findAll();
        assertEquals(0, books.size());

        book.setId(0);
        book = bookRepo.save(book);
        books = bookRepo.findAll();
        assertEquals(1, books.size());
        assertNotEquals(0, book.getId());

        book = bookRepo.findById(book.getId()).orElse(null);
        assertNotNull(book);
        assertEquals("Book Title", book.getTitle());
        assertEquals("John Doe", book.getAuthor().getName());
        assertNotEquals(0, book.getId());
    }

    @Test
    void updateBook() {
        String newTitle = "Title";
        int currentId = book.getId();

        book.setTitle(newTitle);
        book = bookRepo.save(book);
        assertNotNull(book);
        assertEquals(newTitle, book.getTitle());
        assertEquals(currentId, book.getId());
    }

    @Test
    void findBookById() {
        Book found = bookRepo.findById(book.getId()).orElse(null);

        assertNotNull(book);
        assertNotNull(found);
        assertEquals(book.getId(), found.getId());
        assertEquals(book.getAmount(), found.getAmount());
        assertEquals(book.getTitle(), found.getTitle());
        assertEquals(book.getAuthor().getName(), found.getAuthor().getName());
    }

    @Test
    void deleteBook() {
        List<Book> books = bookRepo.findAll();
        assertEquals(1, books.size());

        bookRepo.delete(book);
        books = bookRepo.findAll();
        assertEquals(0, books.size());
    }

    @AfterEach
    void afterEach() {
        bookRepo.deleteAll();
        authorRepo.deleteAll();
    }

}
