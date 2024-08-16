package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Author;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

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
        assertThrows(DataIntegrityViolationException.class, () -> authorRepo.save(nullName));

        Author notUniqueName = new Author();
        notUniqueName.setName(author.getName());
        assertThrows(DataIntegrityViolationException.class, () -> authorRepo.save(notUniqueName));

        Author correctName = new Author();
        correctName.setName("Unique");
        assertDoesNotThrow(() -> authorRepo.save(correctName));
        correctName.setName("Unique2");
        assertDoesNotThrow(() -> authorRepo.save(correctName));
        correctName.setName(author.getName());
        assertThrows(DataIntegrityViolationException.class, () -> authorRepo.save(correctName));
        correctName.setName(null);
        assertThrows(DataIntegrityViolationException.class, () -> authorRepo.save(correctName));
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
        assertNotNull(author);
        Author found = authorRepo.findById(author.getId()).orElse(null);

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

    @Test
    void findAuthorByName() {
        assertNotNull(author);
        Author found = authorRepo.findByName(author.getName()).orElse(null);

        assertNotNull(found);
        assertEquals(author.getId(), found.getId());
        assertEquals(author.getName(), found.getName());
    }

    @AfterEach
    void afterEach() {
        authorRepo.deleteAll();
    }
}
