package com.nerdysoft.rest.service;

import com.nerdysoft.rest.dto.AuthorDTO;
import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.dto.BorrowDTO;
import com.nerdysoft.rest.dto.MemberDTO;
import com.nerdysoft.rest.error.DatabaseOperationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BorrowServiceTest {

    private static BorrowDTO borrow;

    private BookService bookService;
    private AuthorService authorService;
    private MemberService memberService;
    private BorrowService borrowService;

    @Value("${max.borrow.amount}")
    private int maxBorrowAmount;

    @Autowired
    public BorrowServiceTest(BookService bookService,
                             AuthorService authorService,
                             MemberService memberService,
                             BorrowService borrowService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.memberService = memberService;
        this.borrowService = borrowService;
    }

    @BeforeEach
    void beforeEach() {
        MemberDTO member = new MemberDTO();
        member.setName("Name");
        member = memberService.create(member);

        AuthorDTO author = new AuthorDTO();
        author.setName("Author Name");
        BookDTO book = new BookDTO();
        book.setTitle("Title");
        book.setAuthor(author);
        book.setAmount(50);
        book = bookService.create(book);

        borrow = new BorrowDTO();
        borrow.setBook(book);
        borrow.setMember(member);
        borrow = borrowService.create(borrow);
    }

    @Test
    void createBorrow() {
        borrowService.deleteAll();
        List<BorrowDTO> borrows = borrowService.findAll();
        assertEquals(0, borrows.size());

        borrow.setId(0);
        borrow = borrowService.create(borrow);
        borrows = borrowService.findAll();
        assertEquals(1, borrows.size());
        assertNotEquals(0, borrow.getId());
        assertNotNull(borrow.getBook());
        assertNotNull(borrow.getMember());

        BookDTO limited = new BookDTO();
        limited.setTitle("Limited Title");
        limited.setAuthor(borrow.getBook().getAuthor());
        limited = bookService.create(limited);
        BookDTO finalBook = limited;
        assertDoesNotThrow(() -> memberService.borrowBook(borrow.getMember(), finalBook));
        assertThrows(DatabaseOperationException.class,
                () -> memberService.borrowBook(borrow.getMember(), finalBook));

        MemberDTO newMember = new MemberDTO();
        newMember.setName("New Member Name");
        newMember = memberService.create(newMember);
        MemberDTO finalMember = newMember;
        for (int i = 0; i < maxBorrowAmount; i++) {
            memberService.borrowBook(finalMember, borrow.getBook());
        }
        assertThrows(DatabaseOperationException.class,
                () -> memberService.borrowBook(finalMember, borrow.getBook()));
        borrows = borrowService.findByMember(newMember);
        assertEquals(maxBorrowAmount, borrows.size());
    }

    @Test
    void findById() {
        BorrowDTO found = borrowService.findById(borrow.getId()).orElse(null);

        assertNotNull(borrow);
        assertNotNull(found);
        assertEquals(borrow.getId(), found.getId());
        assertEquals(borrow.getBook().getTitle(), found.getBook().getTitle());
        assertEquals(borrow.getMember().getName(), found.getMember().getName());
    }

    @Test
    void findByBook() {
        AuthorDTO author = new AuthorDTO();
        author.setName("Author Name");
        BookDTO book = new BookDTO();
        book.setTitle("Not Set");
        List<BorrowDTO> borrows = borrowService.findByBook(book);
        assertNotNull(borrows);
        assertTrue(borrows.isEmpty());

        borrows = borrowService.findByBook(borrow.getBook());
        assertNotNull(borrows);
        assertFalse(borrows.isEmpty());
        assertEquals(1, borrows.size());
    }

    @Test
    void findByMember() {
        MemberDTO member = new MemberDTO();
        member.setName("Not Set");
        List<BorrowDTO> borrows = borrowService.findByMember(member);
        assertNotNull(borrows);
        assertTrue(borrows.isEmpty());

        borrows = borrowService.findByMember(borrow.getMember());
        assertNotNull(borrows);
        assertFalse(borrows.isEmpty());
        assertEquals(1, borrows.size());
    }

    @Test
    void findByMemberAndBook() {
        MemberDTO member = new MemberDTO();
        member.setName("Not Set");
        AuthorDTO author = new AuthorDTO();
        author.setName("Not Set");
        BookDTO book = new BookDTO();
        book.setTitle("Not Set");
        book.setAuthor(author);
        Optional<BorrowDTO> found = borrowService.findFirstByMemberAndBook(member, borrow.getBook());
        assertTrue(found.isEmpty());

        found = borrowService.findFirstByMemberAndBook(borrow.getMember(), book);
        assertTrue(found.isEmpty());

        found = borrowService.findFirstByMemberAndBook(borrow.getMember(), borrow.getBook());
        assertTrue(found.isPresent());
        assertEquals(borrow.getId(), found.get().getId());
    }

    @Test
    void findAll() {
        List<BorrowDTO> borrows = borrowService.findAll();
        assertNotNull(borrows);
        assertEquals(1, borrows.size());

        BorrowDTO newBorrow = new BorrowDTO();
        newBorrow.setMember(borrow.getMember());
        newBorrow.setBook(borrow.getBook());
        borrowService.create(newBorrow);
        borrows = borrowService.findAll();
        assertEquals(2, borrows.size());
    }

    @Test
    void deleteBorrow() {
        List<BorrowDTO> borrows = borrowService.findAll();
        assertEquals(1, borrows.size());
        borrowService.delete(borrow);
        borrows = borrowService.findAll();
        assertEquals(0, borrows.size());
    }

    @Test
    void deleteAll() {
        BorrowDTO newBorrow = new BorrowDTO();
        newBorrow.setMember(borrow.getMember());
        newBorrow.setBook(borrow.getBook());
        borrowService.create(newBorrow);
        List<BorrowDTO> borrows = borrowService.findAll();
        assertEquals(2, borrows.size());

        borrowService.deleteAll();
        borrows = borrowService.findAll();
        assertTrue(borrows.isEmpty());
    }

    @AfterEach
    void afterEach() {
        borrowService.deleteAll();
        memberService.deleteAll();
        bookService.deleteAll();
        authorService.deleteAll();
    }

}
