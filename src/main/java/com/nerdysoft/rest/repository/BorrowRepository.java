package com.nerdysoft.rest.repository;

import com.nerdysoft.rest.entity.Book;
import com.nerdysoft.rest.entity.Borrow;
import com.nerdysoft.rest.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {

    List<Borrow> findByBook(Book book);

    List<Borrow> findByMember(Member member);

    Optional<Borrow> findFirstByMemberAndBook(Member member, Book book);

}
