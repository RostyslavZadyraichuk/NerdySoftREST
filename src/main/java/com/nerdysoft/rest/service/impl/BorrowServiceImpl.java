package com.nerdysoft.rest.service.impl;

import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.dto.BorrowDTO;
import com.nerdysoft.rest.dto.MemberDTO;
import com.nerdysoft.rest.entity.Book;
import com.nerdysoft.rest.entity.Borrow;
import com.nerdysoft.rest.entity.Member;
import com.nerdysoft.rest.error.DatabaseOperationException;
import com.nerdysoft.rest.repository.BorrowRepository;
import com.nerdysoft.rest.service.BookService;
import com.nerdysoft.rest.service.BorrowService;
import com.nerdysoft.rest.service.MemberService;
import com.nerdysoft.rest.service.mapper.BookMapper;
import com.nerdysoft.rest.service.mapper.BorrowMapper;
import com.nerdysoft.rest.service.mapper.MemberMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class BorrowServiceImpl implements BorrowService {

    private BorrowRepository borrowRepo;
    private BorrowMapper borrowMapper;
    private MemberMapper memberMapper;
    private BookMapper bookMapper;
    private MemberService memberService;
    private BookService bookService;

    @Value("${max.borrow.amount}")
    private int maxBorrowAmount;

    public BorrowServiceImpl(BorrowRepository borrowRepo,
                             BorrowMapper borrowMapper,
                             MemberMapper memberMapper,
                             BookMapper bookMapper,
                             @Lazy MemberService memberService,
                             @Lazy BookService bookService) {
        this.borrowRepo = borrowRepo;
        this.borrowMapper = borrowMapper;
        this.memberMapper = memberMapper;
        this.bookMapper = bookMapper;
        this.memberService = memberService;
        this.bookService = bookService;
    }

    @Override
    public BorrowDTO create(@Valid BorrowDTO borrow) {
        Optional<BookDTO> bookOpt = bookService.findByTitleAndAuthor(
                borrow.getBook().getTitle(), borrow.getBook().getAuthor());
        if (bookOpt.isEmpty()) {
            throw new DatabaseOperationException(String.format("Book with title %s and author %s not found",
                    borrow.getBook().getTitle(), borrow.getBook().getAuthor()));
        }

        Optional<MemberDTO> memberOpt = memberService.findByName(borrow.getMember().getName());
        if (memberOpt.isEmpty()) {
            throw new DatabaseOperationException(String.format("Member with name %s not found",
                    borrow.getMember().getName()));
        }

        BookDTO book = bookOpt.get();
        MemberDTO member = memberOpt.get();

        if (book.getAmount() == 0) {
            throw new DatabaseOperationException(String.format("Book's %s amount is zero",
                    book.getTitle()));
        }

        List<BorrowDTO> borrowed = findByMember(member);
        if (borrowed.size() == maxBorrowAmount) {
            throw new DatabaseOperationException(String.format("Member %s already has %d borrowed books",
                    member.getName(), maxBorrowAmount));
        }

        book.setAmount(book.getAmount() - 1);
        book = bookService.update(book);
        borrow.setBook(book);
        borrow.setMember(member);
        Borrow entity = borrowMapper.toEntity(borrow);
        return borrowMapper.toDTO(borrowRepo.save(entity));
    }

    @Override
    public void delete(BorrowDTO borrow) {
        Optional<BorrowDTO> optional = findById(borrow.getId());
        if (optional.isPresent()) {
            BorrowDTO borrowDTO = optional.get();
            BookDTO book = borrowDTO.getBook();
            book.setAmount(book.getAmount() + 1);
            bookService.update(book);
            Borrow entity = borrowMapper.toEntity(borrow);
            borrowRepo.delete(entity);
        }
    }

    @Override
    public List<BorrowDTO> findAll() {
        return borrowRepo.findAll().stream()
                .map(borrowMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<BorrowDTO> findById(int id) {
        Optional<Borrow> optional = borrowRepo.findById(id);
        if (optional.isPresent()) {
            return Optional.of(borrowMapper.toDTO(optional.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<BorrowDTO> findByMember(MemberDTO member) {
        Member entity = memberMapper.toEntity(member);
        return borrowRepo.findByMemberName(entity.getName()).stream()
                .map(borrowMapper::toDTO)
                .toList();
    }

    @Override
    public List<BorrowDTO> findByBook(BookDTO book) {
        Book entity = bookMapper.toEntity(book);
        return borrowRepo.findByBookTitle(entity.getTitle()).stream()
                .map(borrowMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<BorrowDTO> findFirstByMemberAndBook(MemberDTO member, BookDTO book) {
        Member memberEntity = memberMapper.toEntity(member);
        Book bookEntity = bookMapper.toEntity(book);
        Optional<Borrow> optional = borrowRepo.findFirstByMemberNameAndBookTitle(
                memberEntity.getName(), bookEntity.getTitle());
        if (optional.isPresent()) {
            return Optional.of(borrowMapper.toDTO(optional.get()));
        }
        return Optional.empty();
    }

    @Override
    public void deleteAll() {
        borrowRepo.deleteAll();
    }
}
