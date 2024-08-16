package com.nerdysoft.rest.service.impl;

import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.dto.BorrowDTO;
import com.nerdysoft.rest.dto.MemberDTO;
import com.nerdysoft.rest.entity.Member;
import com.nerdysoft.rest.error.DatabaseOperationException;
import com.nerdysoft.rest.repository.MemberRepository;
import com.nerdysoft.rest.service.BookService;
import com.nerdysoft.rest.service.BorrowService;
import com.nerdysoft.rest.service.MemberService;
import com.nerdysoft.rest.service.mapper.MemberMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepo;
    private BookService bookService;
    private MemberMapper memberMapper;
    private BorrowService borrowService;

    public MemberServiceImpl(MemberRepository memberRepo,
                             BookService bookService,
                             MemberMapper memberMapper,
                             BorrowService borrowService) {
        this.memberRepo = memberRepo;
        this.bookService = bookService;
        this.memberMapper = memberMapper;
        this.borrowService = borrowService;
    }

    @Override
    public MemberDTO create(@Valid MemberDTO member) {
        Optional<Member> optional = memberRepo.findByName(member.getName());
        if (optional.isPresent()) {
            throw new DatabaseOperationException(String.format("Member with name %s already exists",
                    member.getName()));
        }

        member.setMembershipDate(LocalDate.now());
        Member entity = memberMapper.toEntity(member);
        return memberMapper.toDTO(memberRepo.save(entity));
    }

    @Override
    public MemberDTO update(@Valid MemberDTO member) {
        Optional<Member> optional = memberRepo.findByName(member.getName());
        if (optional.isPresent()) {
            throw new DatabaseOperationException(String.format("Member with name %s already exists",
                    member.getName()));
        }

        Member entity = memberMapper.toEntity(member);
        return memberMapper.toDTO(memberRepo.save(entity));
    }

    @Override
    public Optional<MemberDTO> findById(int id) {
        Optional<Member> optional = memberRepo.findById(id);
        if (optional.isPresent()) {
            return Optional.of(memberMapper.toDTO(optional.get()));
        }
        return Optional.empty();
    }

    @Override
    public void delete(MemberDTO member) throws DatabaseOperationException {
        List<BorrowDTO> borrowed = borrowService.findByMember(member);
        if (!borrowed.isEmpty()) {
            throw new DatabaseOperationException(String.format("Member %s has %d borrowed books",
                    member.getName(), borrowed.size()));
        }
        memberRepo.delete(memberMapper.toEntity(member));
    }

    @Override
    public List<MemberDTO> findAll() {
        return memberRepo.findAll().stream()
                .map(memberMapper::toDTO)
                .toList();
    }

    @Override
    public void borrowBook(MemberDTO member, BookDTO book) throws DatabaseOperationException {
        BorrowDTO borrow = new BorrowDTO();
        borrow.setMember(member);
        borrow.setBook(book);
        borrowService.create(borrow);
    }

    @Override
    public void returnBook(MemberDTO member, BookDTO book) {
        Optional<BorrowDTO> optional = borrowService.findFirstByMemberAndBook(member, book);
        if (optional.isPresent()) {
            borrowService.delete(optional.get());
        }
    }

    @Override
    public void deleteAll() throws DatabaseOperationException {
        List<BorrowDTO> borrows = borrowService.findAll();
        if (!borrows.isEmpty()) {
            throw new DatabaseOperationException("Borrow table isn't empty");
        }
        memberRepo.deleteAll();
    }

    @Override
    public Optional<MemberDTO> findByName(String name) {
        Optional<Member> optional = memberRepo.findByName(name);
        if (optional.isPresent()) {
            return Optional.of(memberMapper.toDTO(optional.get()));
        }
        return Optional.empty();
    }
}
