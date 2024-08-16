package com.nerdysoft.rest.service;

import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.dto.BorrowDTO;
import com.nerdysoft.rest.dto.MemberDTO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

public interface BorrowService {

    BorrowDTO create(@Valid BorrowDTO borrow);

    void delete(BorrowDTO borrow);

    List<BorrowDTO> findAll();

    Optional<BorrowDTO> findById(int id);

    List<BorrowDTO> findByMember(MemberDTO member);

    List<BorrowDTO> findByBook(BookDTO book);

    Optional<BorrowDTO> findFirstByMemberAndBook(MemberDTO member, BookDTO book);

    void deleteAll();
}
