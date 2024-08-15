package com.nerdysoft.rest.service.impl;

import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.dto.BorrowDTO;
import com.nerdysoft.rest.dto.MemberDTO;
import com.nerdysoft.rest.entity.Book;
import com.nerdysoft.rest.entity.Borrow;
import com.nerdysoft.rest.entity.Member;
import com.nerdysoft.rest.repository.BorrowRepository;
import com.nerdysoft.rest.service.BorrowService;
import com.nerdysoft.rest.service.mapper.BookMapper;
import com.nerdysoft.rest.service.mapper.BorrowMapper;
import com.nerdysoft.rest.service.mapper.MemberMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowServiceImpl implements BorrowService {

    private BorrowRepository borrowRepo;
    private BorrowMapper borrowMapper;
    private MemberMapper memberMapper;
    private BookMapper bookMapper;

    public BorrowServiceImpl(BorrowRepository borrowRepo,
                             BorrowMapper borrowMapper,
                             MemberMapper memberMapper,
                             BookMapper bookMapper) {
        this.borrowRepo = borrowRepo;
        this.borrowMapper = borrowMapper;
        this.memberMapper = memberMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    public BorrowDTO create(BorrowDTO borrow) {
        Borrow entity = borrowMapper.toEntity(borrow);
        return borrowMapper.toDTO(borrowRepo.save(entity));
    }

    @Override
    public void delete(BorrowDTO borrow) {
        Borrow entity = borrowMapper.toEntity(borrow);
        borrowRepo.delete(entity);
    }

    @Override
    public List<BorrowDTO> findAll() {
        return borrowRepo.findAll().stream()
                .map(borrowMapper::toDTO)
                .toList();
    }

    @Override
    public List<BorrowDTO> findByMember(MemberDTO member) {
        Member entity = memberMapper.toEntity(member);
        return borrowRepo.findByMember(entity).stream()
                .map(borrowMapper::toDTO)
                .toList();
    }

    @Override
    public List<BorrowDTO> findByBook(BookDTO book) {
        Book entity = bookMapper.toEntity(book);
        return borrowRepo.findByBook(entity).stream()
                .map(borrowMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<BorrowDTO> findFirstByMemberAndBook(MemberDTO member, BookDTO book) {
        Member memberEntity = memberMapper.toEntity(member);
        Book bookEntity = bookMapper.toEntity(book);
        Optional<Borrow> optional = borrowRepo.findFirstByMemberAndBook(memberEntity, bookEntity);
        if (optional.isPresent()) {
            return Optional.of(borrowMapper.toDTO(optional.get()));
        }
        return Optional.empty();
    }
}
