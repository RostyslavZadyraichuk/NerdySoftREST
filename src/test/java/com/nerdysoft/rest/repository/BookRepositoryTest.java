package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Author;
import com.nerdysoft.rest.entity.Book;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;

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
    void nameValidation() {
        Book badName = new Book();
        badName.setTitle("Bo");
        badName.setAuthor(book.getAuthor());
        assertThrows(ConstraintViolationException.class, () -> bookRepo.save(badName));

        Book correctName = new Book();
        correctName.setTitle("Book Title");
        correctName.setAuthor(book.getAuthor());
        assertDoesNotThrow(() -> bookRepo.save(correctName));
        correctName.setTitle("Bo");
        assertThrows(TransactionSystemException.class, () -> bookRepo.save(correctName));
        Optional<Book> optional = bookRepo.findById(correctName.getId());
        assertTrue(optional.isPresent());
    }

    @Test
    void amountValidation() {
        Book badAmount = new Book();
        badAmount.setTitle("Book Title");
        badAmount.setAuthor(book.getAuthor());
        badAmount.setAmount(-1);
        assertThrows(ConstraintViolationException.class, () -> bookRepo.save(badAmount));

        Book correctAmount = new Book();
        correctAmount.setTitle("Book Title");
        correctAmount.setAuthor(book.getAuthor());
        correctAmount.setAmount(0);
        assertDoesNotThrow(() -> bookRepo.save(correctAmount));
        correctAmount.setAmount(-1);
        assertThrows(TransactionSystemException.class, () -> bookRepo.save(correctAmount));
        Optional<Book> optional = bookRepo.findById(correctAmount.getId());
        assertTrue(optional.isPresent());

        Book defaultAmount = new Book();
        defaultAmount.setTitle("Book Title");
        defaultAmount.setAuthor(book.getAuthor());
        assertDoesNotThrow(() -> bookRepo.save(defaultAmount));
        Optional<Book> optional2 = bookRepo.findById(defaultAmount.getId());
        assertTrue(optional2.isPresent());
        assertEquals(1, optional2.get().getAmount());
    }

    @Test
    void authorValidation() {
        Book badAuthor = new Book();
        badAuthor.setTitle("Book Title");
        assertThrows(ConstraintViolationException.class, () -> bookRepo.save(badAuthor));

        Book correctAuthor = new Book();
        correctAuthor.setTitle("Book Title");
        correctAuthor.setAuthor(book.getAuthor());
        assertDoesNotThrow(() -> bookRepo.save(correctAuthor));
        correctAuthor.setAuthor(null);
        assertThrows(TransactionSystemException.class, () -> bookRepo.save(correctAuthor));
        Author author = new Author();
        author.setName("John Doe");
        correctAuthor.setAuthor(author);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> bookRepo.save(correctAuthor));
        Optional<Book> optional = bookRepo.findById(correctAuthor.getId());
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
