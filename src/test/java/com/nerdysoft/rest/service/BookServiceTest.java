package com.nerdysoft.rest.service;

import com.nerdysoft.rest.dto.AuthorDTO;
import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.dto.BorrowDTO;
import com.nerdysoft.rest.dto.MemberDTO;
import com.nerdysoft.rest.error.DatabaseOperationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BookServiceTest {

    private static BookDTO book;
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private static Set<ConstraintViolation<BookDTO>> violations;

    private BookService bookService;
    private AuthorService authorService;
    private MemberService memberService;
    private BorrowService borrowService;

    @Autowired
    public BookServiceTest(BookService bookService,
                           AuthorService authorService,
                           MemberService memberService,
                           BorrowService borrowService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.memberService = memberService;
        this.borrowService = borrowService;
    }

    @BeforeAll
    static void beforeAll() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void beforeEach() {
        AuthorDTO author = new AuthorDTO();
        author.setName("John Doe");
        author = authorService.create(author);

        book = new BookDTO();
        book.setTitle("Book Title");
        book.setAuthor(author);
        book = bookService.create(book);
    }

    @Test
    void nameValidation() {
        BookDTO nullAuthor = new BookDTO();
        nullAuthor.setTitle(book.getTitle());
        violations = validator.validate(nullAuthor);
        assertFalse(violations.isEmpty());

        BookDTO badAmount = new BookDTO();
        badAmount.setTitle(book.getTitle());
        badAmount.setAuthor(book.getAuthor());
        badAmount.setAmount(-1);
        violations = validator.validate(badAmount);
        assertFalse(violations.isEmpty());

        BookDTO badTitle = new BookDTO();
        badTitle.setTitle(null);
        badTitle.setAuthor(book.getAuthor());
        violations = validator.validate(badTitle);
        assertFalse(violations.isEmpty());
        badTitle.setTitle("");
        violations = validator.validate(badTitle);
        assertFalse(violations.isEmpty());
        badTitle.setTitle("Th");
        violations = validator.validate(badTitle);
        assertFalse(violations.isEmpty());
        badTitle.setTitle("the book");
        violations = validator.validate(badTitle);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createBook() {
        bookService.deleteAll();
        List<BookDTO> books = bookService.findAll();
        assertEquals(0, books.size());

        book.setId(0);
        AuthorDTO author = new AuthorDTO();
        author.setName("Bob Bob");
        book.setAuthor(author);
        book = bookService.create(book);
        books = bookService.findAll();
        assertEquals(1, books.size());
        assertNotEquals(0, book.getId());
        assertNotEquals(0, book.getAuthor().getId());
        assertEquals(1, book.getAmount());

        int oldId = book.getId();
        book.setId(0);
        book = bookService.create(book);
        assertEquals(oldId, book.getId());
        assertEquals(2, book.getAmount());

        book.setId(0);
        AuthorDTO newAuthor = new AuthorDTO();
        newAuthor.setName("New Author");
        book.setAuthor(newAuthor);
        book = bookService.create(book);
        assertNotEquals(oldId, book.getId());
    }

    @Test
    void findById() {
        BookDTO found = bookService.findById(book.getId()).orElse(null);

        assertNotNull(book);
        assertNotNull(found);
        assertEquals(book.getId(), found.getId());
        assertEquals(book.getAmount(), found.getAmount());
        assertEquals(book.getTitle(), found.getTitle());
        assertEquals(book.getAuthor().getName(), found.getAuthor().getName());
    }

    @Test
    void findByTitle() {
        List<BookDTO> books = bookService.findByTitle("Unknown");
        assertNotNull(books);
        assertTrue(books.isEmpty());

        books = bookService.findByTitle(book.getTitle());
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
    }

    @Test
    void findByAuthor() {
        AuthorDTO author = new AuthorDTO();
        author.setName("Not Set");
        List<BookDTO> books = bookService.findByAuthor(author);
        assertNotNull(books);
        assertTrue(books.isEmpty());

        books = bookService.findByAuthor(book.getAuthor());
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
    }

    @Test
    void findByTitleAndAuthor() {
        AuthorDTO author = new AuthorDTO();
        author.setName("Not Set");
        Optional<BookDTO> found = bookService.findByTitleAndAuthor("Unknown", book.getAuthor());
        assertTrue(found.isEmpty());

        found = bookService.findByTitleAndAuthor(book.getTitle(), author);
        assertTrue(found.isEmpty());

        found = bookService.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        assertTrue(found.isPresent());
        assertEquals(book.getId(), found.get().getId());
    }

    @Test
    void findAllDistinct() {
        BookDTO newBook = new BookDTO();
        newBook.setTitle(book.getTitle());
        AuthorDTO newAuthor = new AuthorDTO();
        newAuthor.setName("Not Set");
        newBook.setAuthor(newAuthor);
        bookService.create(newBook);

        List<BookDTO> books = bookService.findAllDistinct();
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
    }

    @Test
    void findAllBorrowedDistinct() {
        BookDTO newBook = new BookDTO();
        newBook.setTitle(book.getTitle());
        AuthorDTO newAuthor = new AuthorDTO();
        newAuthor.setName("Not Set");
        newBook.setAuthor(newAuthor);
        bookService.create(newBook);

        List<BookDTO> books = bookService.findAllBorrowedDistinct();
        assertNotNull(books);
        assertTrue(books.isEmpty());

        MemberDTO member = new MemberDTO();
        member.setName("Not Set");
        memberService.create(member);
        BorrowDTO borrow = new BorrowDTO();
        borrow.setMember(member);
        borrow.setBook(book);
        borrowService.create(borrow);
        books = bookService.findAllBorrowedDistinct();
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        assertEquals(1, books.get(0).getAmount());

        borrow.setId(0);
        borrow.setBook(newBook);
        borrowService.create(borrow);
        books = bookService.findAllBorrowedDistinct();
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        assertEquals(2, books.get(0).getAmount());
    }

    @Test
    void deleteBook() {
        List<BookDTO> books = bookService.findAll();
        assertEquals(1, books.size());

        MemberDTO member = new MemberDTO();
        member.setName("Member");
        member = memberService.create(member);
        BorrowDTO borrow = new BorrowDTO();
        borrow.setBook(book);
        borrow.setMember(member);
        borrow = borrowService.create(borrow);
        assertThrows(DatabaseOperationException.class, () -> bookService.delete(book));
        BookDTO found = bookService.findByTitleAndAuthor(book.getTitle(), book.getAuthor()).orElse(null);
        assertNotNull(found);

        borrowService.delete(borrow);
        memberService.delete(member);
        bookService.delete(book);
        books = bookService.findAll();
        assertEquals(0, books.size());
    }

    @Test
    void findAll() {
        List<BookDTO> books = bookService.findAll();
        assertNotNull(books);
        assertEquals(1, books.size());

        BookDTO newBook = new BookDTO();
        newBook.setTitle("Second One");
        newBook.setAuthor(book.getAuthor());
        bookService.create(newBook);
        books = bookService.findAll();
        assertEquals(2, books.size());
    }

    @Test
    void deleteAll() {
        BookDTO newBook = new BookDTO();
        newBook.setTitle("Titles");
        newBook.setAuthor(book.getAuthor());
        bookService.create(newBook);
        List<BookDTO> books = bookService.findAll();
        assertNotNull(books);
        assertEquals(2, books.size());

        MemberDTO member = new MemberDTO();
        member.setName("Member");
        member = memberService.create(member);
        BorrowDTO borrow = new BorrowDTO();
        borrow.setBook(book);
        borrow.setMember(member);
        borrow = borrowService.create(borrow);
        assertThrows(DatabaseOperationException.class, () -> bookService.deleteAll());
        BookDTO found = bookService.findByTitleAndAuthor(book.getTitle(), book.getAuthor()).orElse(null);
        assertNotNull(found);

        borrowService.delete(borrow);
        memberService.deleteAll();
        bookService.deleteAll();
        books = bookService.findAll();
        assertEquals(0, books.size());
    }

    @AfterEach
    void afterEach() {
        borrowService.deleteAll();
        memberService.deleteAll();
        bookService.deleteAll();
        authorService.deleteAll();
    }

    @AfterAll
    static void afterAll() {
        validatorFactory.close();
    }

}
