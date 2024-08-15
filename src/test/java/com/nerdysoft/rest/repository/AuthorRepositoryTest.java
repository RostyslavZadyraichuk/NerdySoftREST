package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Author;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AuthorRepositoryTest {

    private static Author author;

    private final AuthorRepository authorRepo;

    @Autowired
    public AuthorRepositoryTest(AuthorRepository authorRepo) {
        this.authorRepo = authorRepo;
    }

    @BeforeEach
    void beforeEach() {
        author = new Author();
        author.setName("John Doe");
        author = authorRepo.save(author);
    }

    @Test
    void nameValidation() {
        Author nullName = new Author();
        assertThrows(ConstraintViolationException.class, () -> authorRepo.save(nullName));

        Author badName = new Author();
        badName.setName("J");
        assertThrows(ConstraintViolationException.class, () -> authorRepo.save(badName));
        badName.setName("Joe joe");
        assertThrows(ConstraintViolationException.class, () -> authorRepo.save(badName));

        Author correctName = new Author();
        correctName.setName("Joe Joe");
        assertDoesNotThrow(() -> authorRepo.save(correctName));
        correctName.setName("First Second");
        assertDoesNotThrow(() -> authorRepo.save(correctName));
        correctName.setName("Bad");
        assertThrows(TransactionSystemException.class, () -> authorRepo.save(correctName));
        Optional<Author> optional = authorRepo.findById(correctName.getId());
        assertTrue(optional.isPresent());
    }

    @Test
    void createAuthor() {
        authorRepo.deleteAll();
        List<Author> authors = authorRepo.findAll();
        assertEquals(0, authors.size());

        author.setId(0);
        author = authorRepo.save(author);
        authors = authorRepo.findAll();
        assertEquals(1, authors.size());
        assertNotEquals(0, author.getId());

        author = authorRepo.findById(author.getId()).orElse(null);
        assertNotNull(author);
        assertEquals("John Doe", author.getName());
        assertNotEquals(0, author.getId());
    }

    @Test
    void updateAuthor() {
        String newName = "Fill Kerr";
        int currentId = author.getId();

        author.setName(newName);
        author = authorRepo.save(author);
        assertNotNull(author);
        assertEquals(newName, author.getName());
        assertEquals(currentId, author.getId());
    }

    @Test
    void findAuthorById() {
        Author found = authorRepo.findById(author.getId()).orElse(null);

        assertNotNull(author);
        assertNotNull(found);
        assertEquals(author.getId(), found.getId());
        assertEquals(author.getName(), found.getName());
    }

    @Test
    void deleteAuthor() {
        List<Author> authors = authorRepo.findAll();
        assertEquals(1, authors.size());

        authorRepo.delete(author);
        authors = authorRepo.findAll();
        assertEquals(0, authors.size());
    }

    @AfterEach
    void afterEach() {
        authorRepo.deleteAll();
    }
}
