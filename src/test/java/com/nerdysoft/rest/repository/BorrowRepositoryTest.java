package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Author;
import com.nerdysoft.rest.entity.Book;
import com.nerdysoft.rest.entity.Borrow;
import com.nerdysoft.rest.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BorrowRepositoryTest {

    private static Borrow borrow;

    private final BorrowRepository borrowRepo;
    private final BookRepository bookRepo;
    private final MemberRepository memberRepo;
    private final AuthorRepository authorRepo;

    @Autowired
    public BorrowRepositoryTest(BorrowRepository borrowRepo,
                                BookRepository bookRepo,
                                MemberRepository memberRepo,
                                AuthorRepository authorRepo) {
        this.borrowRepo = borrowRepo;
        this.bookRepo = bookRepo;
        this.memberRepo = memberRepo;
        this.authorRepo = authorRepo;
    }

    @BeforeEach
    void beforeEach() {
        Author author = new Author();
        author.setName("Author Name");
        author = authorRepo.save(author);

        Book book = new Book();
        book.setTitle("Title");
        book.setAuthor(author);
        book = bookRepo.save(book);

        Member member = new Member();
        member.setName("Member Name");
        member.setMembershipDate(LocalDate.now());
        member = memberRepo.save(member);

        borrow = new Borrow();
        borrow.setBook(book);
        borrow.setMember(member);
        borrow = borrowRepo.save(borrow);
    }

    @Test
    void createBorrow() {
        borrowRepo.deleteAll();
        List<Borrow> borrows = borrowRepo.findAll();
        assertEquals(0, borrows.size());

        borrow.setId(0);
        borrow = borrowRepo.save(borrow);
        borrows = borrowRepo.findAll();
        assertEquals(1, borrows.size());
        assertNotEquals(0, borrow.getId());

        borrow = borrowRepo.findById(borrow.getId()).orElse(null);
        assertNotNull(borrow);
        assertEquals("Member Name", borrow.getMember().getName());
        assertEquals("Title", borrow.getBook().getTitle());
        assertNotEquals(0, borrow.getId());
    }

    @Test
    void findBorrowById() {
        Borrow found = borrowRepo.findById(borrow.getId()).orElse(null);

        assertNotNull(borrow);
        assertNotNull(found);
        assertEquals(borrow.getId(), found.getId());
        assertEquals(borrow.getBook().getTitle(), found.getBook().getTitle());
        assertEquals(borrow.getMember().getName(), found.getMember().getName());
    }

    @Test
    void deleteBorrow() {
        List<Borrow> borrows = borrowRepo.findAll();
        assertEquals(1, borrows.size());

        borrowRepo.delete(borrow);
        borrows = borrowRepo.findAll();
        assertEquals(0, borrows.size());
    }

    @AfterEach
    void afterEach() {
        borrowRepo.deleteAll();
        bookRepo.deleteAll();
        memberRepo.deleteAll();
        authorRepo.deleteAll();
    }

}
