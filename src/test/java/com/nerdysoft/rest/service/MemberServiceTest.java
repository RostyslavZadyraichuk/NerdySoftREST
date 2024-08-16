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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class MemberServiceTest {

    private static MemberDTO member;
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private static Set<ConstraintViolation<MemberDTO>> violations;

    private BookService bookService;
    private AuthorService authorService;
    private MemberService memberService;
    private BorrowService borrowService;

    @Autowired
    public MemberServiceTest(BookService bookService,
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
        member = new MemberDTO();
        member.setName("Name");
        member = memberService.create(member);
    }

    @Test
    void nameValidation() {
        MemberDTO nullMember = new MemberDTO();
        nullMember.setName(null);
        violations = validator.validate(nullMember);
        assertFalse(violations.isEmpty());

        MemberDTO badName = new MemberDTO();
        badName.setName("");
        violations = validator.validate(badName);
        badName.setName("   ");
        violations = validator.validate(badName);
        assertFalse(violations.isEmpty());
    }

    @Test
    void createMember() {
        memberService.deleteAll();
        List<MemberDTO> members = memberService.findAll();
        assertEquals(0, members.size());

        member.setId(0);
        member = memberService.create(member);
        members = memberService.findAll();
        assertEquals(1, members.size());
        assertNotEquals(0, member.getId());
        assertNotNull(member.getMembershipDate());
    }

    @Test
    void findById() {
        MemberDTO found = memberService.findById(member.getId()).orElse(null);

        assertNotNull(member);
        assertNotNull(found);
        assertEquals(member.getId(), found.getId());
        assertEquals(member.getName(), found.getName());
        assertEquals(member.getMembershipDate(), found.getMembershipDate());
    }

    @Test
    void deleteMember() {
        List<MemberDTO> members = memberService.findAll();
        assertEquals(1, members.size());

        AuthorDTO author = new AuthorDTO();
        author.setName("Author Author");
        BookDTO book = new BookDTO();
        book.setTitle("Title");
        book.setAuthor(author);
        book = bookService.create(book);
        BorrowDTO borrow = new BorrowDTO();
        borrow.setBook(book);
        borrow.setMember(member);
        borrow = borrowService.create(borrow);
        assertThrows(DatabaseOperationException.class, () -> memberService.delete(member));
        MemberDTO found = memberService.findByName(member.getName()).orElse(null);
        assertNotNull(found);

        borrowService.delete(borrow);
        memberService.delete(member);
        members = memberService.findAll();
        assertEquals(0, members.size());
    }

    @Test
    void findByName() {
        MemberDTO found = memberService.findByName(member.getName()).orElse(null);

        assertNotNull(member);
        assertNotNull(found);
        assertEquals(member.getId(), found.getId());
        assertEquals(member.getMembershipDate(), found.getMembershipDate());
    }

    @Test
    void borrowBook() {
        AuthorDTO author = new AuthorDTO();
        author.setName("Author Author");
        BookDTO book = new BookDTO();
        book.setTitle("Title");
        book.setAuthor(author);
        book = bookService.create(book);
        assertEquals(1, book.getAmount());

        List<BorrowDTO> borrows = borrowService.findByMember(member);
        assertTrue(borrows.isEmpty());
        memberService.borrowBook(member, book);
        borrows = borrowService.findByMember(member);
        assertFalse(borrows.isEmpty());
    }

    @Test
    void returnBook() {
        AuthorDTO author = new AuthorDTO();
        author.setName("Author Author");
        BookDTO book = new BookDTO();
        book.setTitle("Title");
        book.setAuthor(author);
        book = bookService.create(book);
        assertEquals(1, book.getAmount());

        List<BorrowDTO> borrows = borrowService.findByMember(member);
        assertTrue(borrows.isEmpty());
        memberService.borrowBook(member, book);
        borrows = borrowService.findByMember(member);
        assertFalse(borrows.isEmpty());

        memberService.returnBook(member, book);
        borrows = borrowService.findByMember(member);
        assertTrue(borrows.isEmpty());
    }

    @Test
    void findAll() {
        List<MemberDTO> members = memberService.findAll();
        assertNotNull(members);
        assertEquals(1, members.size());

        MemberDTO newMember = new MemberDTO();
        newMember.setName("Second One");
        memberService.create(newMember);
        members = memberService.findAll();
        assertEquals(2, members.size());
    }

    @Test
    void deleteAll() {
        MemberDTO newMember = new MemberDTO();
        newMember.setName("New Name");
        memberService.create(newMember);
        List<MemberDTO> members = memberService.findAll();
        assertNotNull(members);
        assertEquals(2, members.size());

        AuthorDTO author = new AuthorDTO();
        author.setName("Author Author");
        BookDTO book = new BookDTO();
        book.setTitle("Title");
        book.setAuthor(author);
        book = bookService.create(book);
        BorrowDTO borrow = new BorrowDTO();
        borrow.setBook(book);
        borrow.setMember(member);
        borrow = borrowService.create(borrow);
        assertThrows(DatabaseOperationException.class, () -> memberService.deleteAll());
        MemberDTO found = memberService.findByName(member.getName()).orElse(null);
        assertNotNull(found);

        borrowService.delete(borrow);
        memberService.deleteAll();
        bookService.deleteAll();
        members = memberService.findAll();
        assertEquals(0, members.size());
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
