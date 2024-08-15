package com.nerdysoft.rest.service;

import com.nerdysoft.rest.dto.BookDTO;
import com.nerdysoft.rest.dto.MemberDTO;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    MemberDTO create(MemberDTO member);

    MemberDTO update(MemberDTO member);

    Optional<MemberDTO> findById(int id);

    void delete(MemberDTO member);

    List<MemberDTO> findAll();

    void borrowBook(MemberDTO member, BookDTO book);

    void returnBook(MemberDTO member, BookDTO book);

    void deleteAll();

    Optional<MemberDTO> findByName(String name);

}
