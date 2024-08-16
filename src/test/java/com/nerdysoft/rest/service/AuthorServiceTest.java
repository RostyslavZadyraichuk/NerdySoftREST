package com.nerdysoft.rest.service;

import com.nerdysoft.rest.dto.AuthorDTO;
import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.error.DatabaseOperationException;
import jakarta.validation.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AuthorServiceTest {

    private static AuthorDTO author;
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private static Set<ConstraintViolation<AuthorDTO>> violations;

    private AuthorService authorService;
    private BookService bookService;

    @Autowired
    public AuthorServiceTest(AuthorService authorService,
                             BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @BeforeAll
    static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        author = new AuthorDTO();
        author.setName("John Doe");
        author = authorService.create(author);
    }

    @Test
    void nameValidation() {
        AuthorDTO nullName = new AuthorDTO();
        violations = validator.validate(nullName);
        assertFalse(violations.isEmpty());

        AuthorDTO badName = new AuthorDTO();
        badName.setName("Ao");
        violations = validator.validate(badName);
        assertFalse(violations.isEmpty());
        badName.setName("Joe joe");
        violations = validator.validate(badName);
        assertFalse(violations.isEmpty());

        AuthorDTO correctName = new AuthorDTO();
        correctName.setName("Joe Joe");
        violations = validator.validate(correctName);
        assertTrue(violations.isEmpty());
    }

    @Test
    void createAuthor() {
        authorService.deleteAll();
        List<AuthorDTO> authors = authorService.findAll();
        assertEquals(0, authors.size());

        author.setId(0);
        author = authorService.create(author);
        authors = authorService.findAll();
        assertEquals(1, authors.size());
        assertNotEquals(0, author.getId());

        author = authorService.findByName(author.getName()).orElse(null);
        assertNotNull(author);
        assertEquals("John Doe", author.getName());
        assertNotEquals(0, author.getId());
    }

    @Test
    void deleteAuthor() {
        List<AuthorDTO> authors = authorService.findAll();
        assertEquals(1, authors.size());

        BookDTO book = new BookDTO();
        book.setTitle("Title");
        book.setAuthor(author);
        book = bookService.create(book);
        assertThrows(DatabaseOperationException.class, () -> authorService.delete(author));
        AuthorDTO found = authorService.findByName(author.getName()).orElse(null);
        assertNotNull(found);

        bookService.delete(book);
        authorService.delete(author);
        authors = authorService.findAll();
        assertEquals(0, authors.size());
    }

    @Test
    void findAuthorByName() {
        assertNotNull(author);
        AuthorDTO found = authorService.findByName(author.getName()).orElse(null);

        assertNotNull(found);
        assertEquals(author.getId(), found.getId());
        assertEquals(author.getName(), found.getName());
    }

    @Test
    void findAll() {
        List<AuthorDTO> authors = authorService.findAll();
        assertNotNull(authors);
        assertEquals(1, authors.size());

        AuthorDTO newAuthor = new AuthorDTO();
        newAuthor.setName("Second One");
        authorService.create(newAuthor);
        authors = authorService.findAll();
        assertEquals(2, authors.size());
    }

    @Test
    void deleteAll() {
        AuthorDTO newAuthor = new AuthorDTO();
        newAuthor.setName("Second One");
        authorService.create(newAuthor);
        List<AuthorDTO> authors = authorService.findAll();
        assertNotNull(authors);
        assertEquals(2, authors.size());

        BookDTO book = new BookDTO();
        book.setTitle("Title");
        book.setAuthor(author);
        bookService.create(book);
        assertThrows(DatabaseOperationException.class, () -> authorService.deleteAll());
        AuthorDTO found = authorService.findByName(author.getName()).orElse(null);
        assertNotNull(found);

        bookService.deleteAll();
        authorService.deleteAll();
        authors = authorService.findAll();
        assertEquals(0, authors.size());
    }

    @AfterEach
    void afterEach() {
        authorService.deleteAll();
    }

    @AfterAll
    static void afterAll() {
        validatorFactory.close();
    }

}
